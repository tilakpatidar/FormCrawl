import urlparse

import re
import hashlib
import scrapy
from bs4 import BeautifulSoup as bs
from elasticsearch import Elasticsearch


def generate_form_id(form_data):
    convert_to_space = re.sub('\t|\n|\r', " ", form_data)
    replaced_spaces = re.sub('\s+', "", convert_to_space)
    return hashlib.md5(replaced_spaces).hexdigest()


class FormSpider(scrapy.Spider):
    name = "form_crawler"

    def start_requests(self):
        urls = open("seed.txt").read().split("\n")
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        soup = bs(response.body)
        title = soup.find("title").text
        for url in response.xpath('//a/@href').extract():
            abs_url = urlparse.urljoin(response.url, url.strip())
            yield scrapy.Request(abs_url, callback=self.parse)

        for form in soup.findAll("form"):
            for script in form.find_all("script"):
                script.extract()
            for style in form.find_all("style"):
                style.extract()
            form_data = str(form)
            idd = generate_form_id(form_data)
            input_attrs = form.find_all(attrs={"name": True})
            placeholder_attrs = form.find_all(attrs={"placeholder": True})
            names = []
            placeholders = []
            btn_text = []
            for input in input_attrs:
                try:
                    names.append(input["name"])
                except:
                    pass
            for placeholder in placeholder_attrs:
                try:
                    placeholders.append(placeholder["placeholder"])
                except:
                    pass
            for btn in form.find_all(attrs={"type": "submit"}):
                try:
                    btn_text.append(btn["value"])
                except:
                    pass
            for btn in form.find_all("button"):
                try:
                    btn_text.append(btn.text)
                except:
                    pass
            text = re.sub("\s+|\n|\r|\t", ' ', form.getText(separator=u' '))
            item = {'title': title,
                    'url': response.url,
                    'form': form_data,
                    'text': text,
                    'name': names,
                    'placeholders': placeholders,
                    'btn_text': btn_text
                    }
            es = Elasticsearch()
            es.index(index="alexa_500", doc_type="form", id=idd, body=item)
            yield item
