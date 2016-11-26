
setInterval(function(){
  var toRemove=[];
  toRemove.push.apply(toRemove, document.querySelectorAll('link[type*="css"]'));
  toRemove.push.apply(toRemove, document.querySelectorAll('style'));
  toRemove.push.apply(toRemove, document.querySelectorAll('img'));
  toRemove.push.apply(toRemove, document.querySelectorAll('canvas'));
  toRemove.forEach(function(s){
    s.parentNode.removeChild(s);
  });

  [].forEach.call(document.querySelectorAll('[style]'), function(e){

    e.removeAttribute('style');
  });

  var stylesheets = document.styleSheets;
  var len = stylesheets.length;
  while(len!=0){
    var sh = stylesheets[len - 1];

    var count = sh.rules.length;

    while(count!=0)  {
      sh.deleteRule(0);
      count = sh.rules.length;
    }
    len--;
  }

  var km = ['click', 'dblclick', 'mousedown', 'mousemove', 'mouseover', 'mouseout', 'mouseup', 'mouseenter', 'mouseleave', 'keydown', 'keypress', 'keyup', 'scroll'];
  function preventAll(parent){
    var dom = parent.getElementsByTagName('*');
    for(var i=0,l=dom.length; i<l; i++){
      for(var n=0,c=km.length; n<c; n++){
        dom[i]['on'+km[n]] = function(e){
          e = e || event;
          e.preventDefault();
          return false;
        }
      }
    }
    var fr = frames;
    for(var i=0,l=fr.length; i<l; i++){
      // cancell frames events here
    }
  }
  preventAll(document);


  for(var n=0,c=km.length; n<c; n++){
    window['on'+km[n]] = function(e){
      e = e || event;
      e.preventDefault();
      return false;
    }
  }

}, 1000);
