$(document).ready(function() {
	$('[custom-annotation]').each(function() {
		console.log($(this));
		enableAnnotation($(this));
	});

	$(window).click(function(event) {
		if (!$(event.target).is('[custom-annotation]')) {
			$("#annotation").remove();
		}
	});
});

function enableAnnotation(element) {
	element.css({"text-decoration": "underline"});
	
	element.click(function(event) {
		$("body").append($('<div id="annotation"></div>'));
		$("#annotation").text(element.attr("custom-annotation"));
		$("#annotation").position({
			my: "left top",
			at: "left bottom",
			of: event, // or $("#otherdiv")
			collision: "flip"
		});
		$("#annotation").show();
	});
}
