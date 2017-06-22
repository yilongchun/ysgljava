// JavaScript Document
jQuery(document).ready(
		function() {
			jQuery('#Top .Toolbar1 .CentreBox .Menu .List1 li').click(
					function() {
						var index = jQuery(this).parent().children()
								.index(this);
						jQuery(this).parent().children().each(function() {
							if (jQuery(this).hasClass('Select')) {
								jQuery(this).removeClass('Select');
							}
						});
						jQuery(this).addClass('Select');

						jQuery('#Top .Toolbar2 .CentreBox .Menu').each(
								function() {
									if (!jQuery(this).hasClass('Hide')) {
										jQuery(this).addClass('Hide');
									}
								});
						jQuery('#Top .Toolbar2 .CentreBox .Menu').eq(index)
								.removeClass('Hide');
					});

			jQuery('#Top .Toolbar2 .CentreBox .Menu ul li a').mouseenter(
					function() {
						var index = jQuery(
								'#Top .Toolbar2 .CentreBox .Menu ul li a')
								.index(this);
						jQuery('#Top .Toolbar2 .CentreBox .Menu ul li').each(
								function() {
									if (jQuery(this).hasClass('Select')) {
										jQuery(this).removeClass('Select');
									}
								});
						jQuery(this).parent().addClass('Select');
					});
		});