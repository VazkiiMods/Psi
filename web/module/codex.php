<div class='section-header'>
	<span class='glyphicon glyphicon-book'></span>
	Taurus Silver's Codex Online
</div>

<div class='warning-txt'>
	<ul>
		<li>This page is a verbatim copy of the Taurus Silver's Codex you can access ingame by pressing the Psi master keybind. It does not, however, contain any recipes or images. Usage of the in-game guide is encouraged.</li>
		<li>The contents of this page are fed from the <a href="https://github.com/Vazkii/Psi/blob/master/src/main/resources/assets/psi/lang/en_us.json">bleeding-edge lang file</a> and may be ahead of the Psi version currently out.</li>
		<li>This page contains spoilers for unlockables!</li>
	</ul>
</div>

<?php
	$file = file_get_contents(count($argv) >= 2 ? $argv[1] : "https://raw.githubusercontent.com/Vazkii/Psi/master/src/main/resources/assets/psi/lang/en_us.json");
	$json = json_decode($file, true);
	$default_keys = [
		'psimisc.keybind' => 'c',
		'use' => 'Right Button'
	];
	$entry_cache = array();
	$replacements = [
		'/\$\((br|p)\)/' => "<br />",
		'/\$\(li\)/' => " &bull; ",
		'/\$\(([1-9a-o])\)([^$]+)(?:\$\([0r]?\)|(?=<)|$)/' => "<span class='mc-color-$1'>$2</span>",
		'/\$\(thing\)([^$]+)(?:\$\([0r]?\)|(?=<)|$)/' => "<span class='color-thing'>$1</span>",
		'/\$\(item\)([^$]+)(?:\$\([0r]?\)|(?=<)|$)/' => "<span class='color-item'>$1</span>",
		'/\$\(piece\)([^$]+)(?:\$\([0r]?\)|(?=<)|$)/' => "<span class='mc-color-b'>$1</span>",
	];
	
	$opened_div = false;
	
	$current_entry = '';
	foreach($json as $key => $value) {
		$entry_match = match_entry($key);
		$page_match = match_page($key, $current_entry);
		if(sizeof($entry_match) > 0) {
			$current_entry = $entry_match[1];
			if($opened_div)
				echo('</div>');
			echo("<br><span id='$current_entry' class='bookmark-anchor'></span>");
			echo("<a href='#$current_entry' class='entry-bookmark glyphicon glyphicon-bookmark' title='Permalink'></a> ");
			echo("<b id='$current_entry-fake'>$value</b><div class='entry-text'>");
			echo("\n");
			$opened_div = true;
		}
		if(sizeof($page_match) > 0) {
			$page = str_replace("%%", "%", $value);
			$oldpage = null;
			while(strcmp($page, $oldpage) != 0) {
				$oldpage = $page;
				$page = preg_replace_callback('/\$\(k:([^)]*)\)/', 'key_resolve',
					preg_replace_callback('/\$\(l:([^)]+)\)([^$]+)\$\(\/l\)/', 'link_resolve',
					preg_replace(array_keys($replacements), array_values($replacements),
					$page)));
			}
			$no_control = preg_replace('/\$\([^)]*\)/', '', $value);
			if(strlen($no_control) > 50 || strcmp($no_control, "no") == 0) {
				echo($page . '<br /><br />');
				echo("\n");
			}
		}
	}
	
	echo('</div>');
	
	function match($regex, $line) {
		$matches = array();
		preg_match("/$regex/", $line, $matches);
		return $matches;
	}
	
	function match_entry($key) {
		return match('psi\.book\.entry\.(\w+)', $key);
	}
	
	function match_page($key, $entry_name) {
		return match("psi\.book\.page\.$entry_name\.\d+", $key);
	}
	function key_resolve($match) {
		global $default_keys; // i'm sorry, i don't know php
		return (array_key_exists($match[1], $default_keys)
			? $default_keys[$match[1]]
			: "UNKNOWN KEY");
	}
	function link_resolve($match) {
		$link = $match[1];
		$res = [];
		if(preg_match('|^\w+/(\w+)(#\w+)?$|', $link, $res)) {
			$entry_name = preg_replace_callback('/_(\w)/', function($repl) {
				return strtoupper($repl[1]);
			}, $res[1]);
			$link = "#$entry_name";
		}
		return "<a href='$link'>$match[2]</a>";
	}
?>
