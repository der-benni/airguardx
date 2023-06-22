$(window).on('load', function () {

    $('.radio-input').on('click', function() {
        $.ajax({
            url: '/setProfile',
            type: "POST",
            data: JSON.stringify({ "profile": $(this).data('profile') }),
            dataType: 'application/json',
            contentType: 'application/json'
        });
    });
});