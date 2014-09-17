$(document).ready(function () {
    $("#dialog-delete-confirm").dialog({
        autoOpen: false,
        resizable: false,
        height: 160,
        modal: true,
        buttons: {
            "Delete": function () {
                var thisParentId = $('#dialog-delete-confirm').data('thisParentId');
                $.post('/properties/delete.html', {
                    id: thisParentId
                }).done(function () {
                        $('#' + thisParentId).fadeOut();
                    });
                delete thisParentId;
                jQuery.removeData($('#dialog-delete-confirm'));
                $(this).dialog("close");
            },
            Cancel: function () {
                $(this).dialog("close");
            }
        }
    });


    $(".propertiesDelete").bind('click', function () {
        var thisParentId = $(this).parent().parent().attr('id');
        $('#dialog-delete-confirm').data("thisParentId", thisParentId);
        $('#dialog-delete-confirm').dialog("open");
    });

    $('#dialog-form').dialog({
        autoOpen: false,
        height: 200,
        width: 350,
        modal: true,
        title: "Edit Property",
        show: 'clip',
        hide: 'clip',
        modal: true,

        buttons: [
            {
                text: "Submit",
                click: function () {
                    var dialog = $('#dialog-form');
                    var propValueOld = dialog.data('PROPERTY_VALUE_OLD');
                    var propNameOld = dialog.data('PROPERTY_NAME_OLD');
                    var thisParentId = dialog.data('thisParentId');

                    $.post('/properties/edit.html', {
                        id: thisParentId,
                        name: $('#propertyNameNew').val(),
                        value: $('#propertyValueNew').val()
                    });
                    updateEditProperties(propNameOld, propValueOld);
                    jQuery.removeData(dialog);
                    shutdownEditProperties.call($(this));
                }
            },
            {
                text: "Cancel",
                click: function () {
                    shutdownEditProperties.call($(this));
                }
            }
        ]
    });

    function updateEditProperties(propertyName, propertyValue) {
        function updateEditProperty(propertyName, propertyPlace) {
            $(propertyName).fadeOut()
                .queue(function (n) {
                    $(propertyName).html(propertyPlace)
                    n();
                }).fadeIn();
        }

        updateEditProperty(propertyName, $("#propertyNameNew").val());
        updateEditProperty(propertyValue, $("#propertyValueNew").val());
    }

    function shutdownEditProperties() {
        $('#propertyForm').find(':input').each(function () {
            switch (this.type) {
                case 'password':
                case 'select-multiple':
                case 'select-one':
                case 'text':
                case 'textarea':
                    $(this).val('');
                    break;
                case 'checkbox':
                case 'radio':
                    this.checked = false;
            }
        });
        $(this).dialog("close");
    }

    $(".propertiesEdit").bind('click', function () {
        var thisParentId = $(this).parent().parent().attr('id');
        var PROPERTY_VALUE_OLD = "tr#" + thisParentId + " td:nth-child(2)";
        var PROPERTY_NAME_OLD = "tr#" + thisParentId + " td:nth-child(1)";
        updateFormProperties();
        $('#dialog-form').data("PROPERTY_VALUE_OLD", PROPERTY_VALUE_OLD);
        $('#dialog-form').data("PROPERTY_NAME_OLD", PROPERTY_NAME_OLD);
        $('#dialog-form').data("thisParentId", thisParentId);
        $('#dialog-form').dialog("open");

        function updateFormProperties() {
            $('#propertyNameNew').val($(PROPERTY_NAME_OLD).html());
            $('#propertyValueNew').val($(PROPERTY_VALUE_OLD).html());
            $('#id').val(thisParentId);
        }

        return false;
    });
});