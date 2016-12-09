(function() {
    /**
     * get the date from the date fields in the 
     */
    window.isMondayOrFriday = function(dateId) {
        var date = dateValue(dateId);
        if(!date) {
            return;
        }
        
        // Sunday = 0, Monday = 1, Tuesday = 2, Wednesday = 3, Thursday = 4, Friday = 5, Saturday = 6
        if(date.getDay() === 1 || date.getDay() === 5) {
            return true;
        } 
        return false;
     };
        
    window.dateValue = function(dateId) {
        if(!dateId) {
            return;
        }
        
        var day = $('#' + dateId + '_day').val();
        var month = $('#' + dateId + '_month').val();
        var year = $('#' + dateId + '_year').val();
        
        if(!day || day === '' || !month || month === '' || !year || year === '') {
            return null;
        }

        try {
            var dayInt = parseInt(day, 10);
            var monthInt = parseInt(month, 10);        
            var yearInt = parseInt(year, 10);
            
            var date = new Date(yearInt, monthInt -1, dayInt, 0, 0, 0, 0);
            return date;
        } catch(e) {
            debugger
            // invalid date
            console.error('problems parsing date: ' + e.message);
            return null;
        }
    };
    
}).call(this);
