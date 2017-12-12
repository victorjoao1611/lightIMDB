$(document).ready(function() {
    $("#formUpload").hide();
    $("#formComment").hide();
    $("#formEvaluate").hide();

    $("#showUpload").click(function(){
        if ($("#formUpload").is(':visible')) {
            $("#formUpload").hide();
        } else {
            $("#formUpload").show();
        }
    });
    $("#showComment").click(function(){
        if ($("#formComment").is(':visible')) {
            $("[name='Email']").show();
            $("[name='Titulo']").show();
            $("#formComment").hide();
        } else {
            $("#formComment").show();
            $("[name='Email']").hide();
            $("[name='Titulo']").hide();
        }
    });
    $("#showEvaluate").click(function(){
        if ($("#formEvaluate").is(':visible')) {
            $("[name='Titulo']").show();
            $("[name='QuantAvaliacoes']").show();
            $("#formEvaluate").hide();
        } else {
            $("#formEvaluate").show();
            $("[name='Titulo']").hide();
            $("[name='QuantAvaliacoes']").hide();
        }
    });
});