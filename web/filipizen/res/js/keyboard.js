function setCaretPosition(elemId, caretPos) {
    var elem = document.getElementById(elemId);

    if(elem != null) {
        if(elem.createTextRange) {
            var range = elem.createTextRange();
            range.move('character', caretPos);
            range.select();
        }
        else {
            if(elem.selectionStart) {
                elem.focus();
                elem.setSelectionRange(caretPos, caretPos);
            }
            else
                elem.focus();
        }
    }
}

$(function(){
    var $write = $('#write');
     
    $('#keyboard li button').click(function(){
        var $this = $(this),
            character = $this.html();
        var caret = document.getElementById('write').selectionStart;
        var caretEnd = document.getElementById('write').selectionEnd;
        var characters = $this.val();
        var textarea = $('#write').val(); 

        $("#wrtie").val(textarea.substring(0, caret) + character + textarea.substring(caretEnd) );
        setCaretPosition('write', caret+1);


         
        // clear
        if ($this.hasClass('clear')) {
            var html = $write.html();
             
            $write.html(html.substr(0, html.length - 500));
            return false;
        } 

        // Delete
        if ($this.hasClass('delete')) {
            var html = $write.html();
             
            $write.html(html.substr(0, html.length - 1));
            return false;
        }   
         
        // Special characters
        if ($this.hasClass('symbol')) character = $('span:visible', $this).html();
        if ($this.hasClass('space')) character = ' ';

        // Add the character
        $write.html($write.html() + character);
        $write.focus();
    });
});

