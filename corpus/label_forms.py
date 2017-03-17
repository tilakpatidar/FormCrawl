import json
import re

file = open("forms.json")
file_data = file.read()
forms = file_data.split("\n")
stop_words = open("stopwords_en.txt").read().replace("\r","").split("\n")

s_file = open('s.txt', 'a')
l_file = open('l.txt', 'a')
r_file = open('r.txt', 'a')
o_file = open('o.txt', 'a')
c_file = open('c.txt', 'a')
m_file = open('m.txt', 'a')
p_file = open('p.txt', 'a')
def convert_snake_case(text):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', text)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()

def convert_snake_case_to_text(text):
    return " ".join(text.split("_"))

def filter_text(text):
    text = re.sub(r'[^a-zA-Z0-9\s]', ' ', text).lower()
    text = re.sub(r'\d+', ' ', text)
    li = re.sub(r'\s+', ' ', text)
    temp = []
    for l in li.split(" "):
        if l not in stop_words:
            temp.append(l)
    return " ".join(temp)

for form in forms:
    try:
        form_data = json.loads(form)["_source"]
        if "facebook.com" in form_data["url"]:
            continue
        names = convert_snake_case(" ".join(form_data["name"]))
        names = convert_snake_case_to_text(names)
        title = form_data['title']
        placeholder = " ".join(form_data["placeholders"])
        btn_text = " ".join(form_data["btn_text"])
        text = form_data["text"]
        text = filter_text(names + " " + placeholder + " " + btn_text + " " + text + " " + title)
        print "#################################"
        print "(S) search |(L) Login | (R) Registration | (O) Other | (C) Contact | (M) Mailing List | (P) Password Recovery | (X) skip"
        print form_data['url']
        print title
        print text
        choice = str(raw_input()).lower()
        if(choice == 'x'):
            continue
        print choice
        new_line = '\n'
        line_to_write = choice + ",'" + text + "'" + new_line
        eval(choice + "_file.write(line_to_write)")
        eval(choice + "_file.flush()")
        print "#################################"
    except Exception as e:
        print e


