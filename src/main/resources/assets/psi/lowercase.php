<?php
	fix('.');

	function fix($path) {
		echo "Fixing directory $path\n";

		$files = scandir($path);
		echo "Found files " . implode($files, ' '). "\n\n";

		foreach($files as $file) {
			if(strlen($file) <= 2)
				continue;

			$nextPath = "$path/$file";

			if(is_dir($nextPath))
				fix($nextPath);
			else lowercase($nextPath);
		}
	}

	function lowercase($file) {
		$ext = substr($file, strrpos($file, '.'));
		if($ext === '.lang')
			return;

		$contents = file_get_contents($file);
		$newContents = $contents;
		if($ext === '.json')
			$newContents = lowercaseString($contents);

		$newName = lowercaseString($file);
		file_put_contents($newName, $newContents);

		if($newName != $file)
			unlink($file);
	}

	function lowercaseString($str) {
		return strtolower(preg_replace("/([A-Z])/", '_$1', $str));
	}
?>