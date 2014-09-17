<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>

<div id="dialog-form-add-muleserver" title="Create new Server">
    <p class="validateTips">All form fields are required.</p>

    <form id="createNewServerForm">
        <fieldset style="border: 0">
            <label for="name">Name</label>
            <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all input"/>

            <label for="serverAddress">Host or IP address</label>
            <input type="text" name="serverAddress" id="serverAddress" value=""
                   class="text ui-widget-content ui-corner-all input"/>

            <label for="prefix">Mule Prefix</label>
            <input type="text" name="prefix" id="prefix" value="" class="text ui-widget-content ui-corner-all input"/>

        </fieldset>
    </form>
</div>

<script>
    $("#dialog-form-add-muleserver").dialog({
        autoOpen: false,
        height: 350,
        width: 350,
        modal: true,
        buttons: {
            "Create a Server": function () {
                $.post('/mule/server/add.html', {
                    name: $('#name').val(),
                    serverAddress: $('#serverAddress').val(),
                    prefix: $('#prefix').val(),
                    deployDirectory: $('#deployDirectory').val()
                });
                $(this).dialog("close");
            },
            Cancel: function () {
                $(this).dialog("close");
            }
        },
        close: function () {
            $('#createNewServerForm').trigger('reset');
        }
    });

    $("#create-user").button()
            .click(function () {
                $("#dialog-form-add-muleserver").dialog("open");
            });
</script>