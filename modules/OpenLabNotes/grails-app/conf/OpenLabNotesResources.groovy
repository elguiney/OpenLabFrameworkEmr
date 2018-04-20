modules = {
	tinymce{
		dependsOn 'jquery'
		disposition: 'head'
		resource url: 'js/tinymce/tinymce.min.js'
		resource url: 'js/tinymce/skins/lightgray/skin.min.css'
		resource url: 'js/tinymce/skins/lightgray/content.min.css'
		resource url: 'js/tinymce/themes/modern/theme.min.js'
		resource url: 'js/tinymce/plugins/table/plugin.min.js'
		resource url: 'js/tinymce/plugins/textcolor/plugin.min.js'
		resource url: 'js/tinymce/plugins/code/plugin.min.js'
        resource url: 'js/tinymce/plugins/fullscreen/plugin.min.js'
        resource url: 'js/tinymce/plugins/print/plugin.min.js'
		resource url: 'js/tinymce/plugins/paste/plugin.min.js'
        resource url: 'js/tinymce/plugins/contextmenu/plugin.min.js'
	}
}