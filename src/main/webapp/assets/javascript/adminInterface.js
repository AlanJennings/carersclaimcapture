var adminInterface = (function () {

    // make all the questions draggable
    
    var init = function () {
        $("[data-tag-type]").not("[data-tag-type='page']").not("[data-tag-nested='true']").draggable({
            start: function(event, ui) { 
                $(this).css("z-index", 1000);
            },
            stop: function(event, ui) {
                $(this).css("z-index", 'auto');
            }
        });
        
      $("[data-tag-type='page']").add("[data-tag-type='container']").droppable({
          drop: function(event, ui) {
              var droppedComponent = ui.helper;
              console.log('moving "' + droppedComponent.find("label").text().trim() + '"');
              var page = droppedComponent.closest("[data-tag-type='page']");
              var components = page.find("[data-tag-type]").not("[data-tag-type='page']").not("[data-tag-nested='true']");
              _sortVertically(components);

//              var insertAfterComponent = components.slice(3,4);
//              droppedComponent.insertAfter(insertAfterComponent);
//              droppedComponent.css('top', 'auto').css('left', 'auto');
          
              var top = ui.offset.top;
              console.log("dropped at " + top);
              for(var index = 0; index < components.length; index++) {
                  var component = $(components[index]);
                  var componentName = component.find('label').text().trim();
                  console.log('Checking against "' + componentName + '"');
                  var componentTop = component.position().top;
                  console.log('componentTop = ' + componentTop);
                  if(componentTop > top) {
                      // note jquery 'is' only matches exactly for single objects
                      if(component.is(droppedComponent)) {
                          console.log('skipping self');
                          continue;
                      }
                      console.log('found position, inserting before existing component');
                      // the dropped component has been dropped below this one, so insert it here
                      droppedComponent.insertBefore(component);
                      // reset the css position
                      console.log('resetting css position');
                      droppedComponent.css('top', 'auto').css('left', 'auto');
                      break;
                  }
              }
              console.log('done moving component');
          }
//          drop: function( event, ui ) {
//              // rearrange the components so that they are in a specific order, so reset the horiziontal
//              // offset and move everything else to fit
//              var that = $(this);
//              var page = that.closest("[data-tag-type='page']");
//              var components = page.find("[data-tag-type]").not("[data-tag-type='page']").not("[data-tag-nested='true']");
//              _sortVertically(components);
//              
//              var top = ui.offset.top;
//              for(var index = 0; index < components.length; index++) {
//                  var component = $(components[index]);
//                  var componentName = component.find('label').text().trim();
//                  var componentTop = component.position().top;
//                  if(component != that && componentTop > top) {
//                      // the dropped component has been dropped below this one, so insert it here
//                      that.insertBefore(component);
//                      // reset the css position
//                      that.css('top', 'auto').css('left', 'auto');
//                      break;
//                  }
//              }
//          }
        });
    };

    var _sortVertically = function (componentArray) {
        if(!componentArray) {
            return;
        }
        
        if(componentArray.length < 2) {
            return;
        }
        
        componentArray.sort(function(first, second) {
            if(first == second) {
                return 0;
            }
            if(!first) {
                return 1;
            }
            if(!second) {
                return -1;
            }
            
            var firstTop = $(first).position().top;
            var secondTop = $(second).position().top;
            return firstTop - secondTop;
        });
    };
    
    var anotherMethod = function () {
        // public
    };
  
    return {
        init: init
    };

})();