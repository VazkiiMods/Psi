<!DOCTYPE html>
<html>
	<title>Psi</title>	
	<head>
		<link rel="shortcut icon" type="image/png" href="favicon.ico"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="css/ripples.min.css">
        <link rel="stylesheet" href="css/material-wfont.min.css">
		<link rel="stylesheet" href="css/psi.css">
		<link rel="stylesheet" href="css/minecraft-pallette.css">
	</head>

	<body>
		<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		  <div class="container" id="navbar-contents">
		  <ul class="nav navbar-nav navbar-nav-left">
			<li id="header-btn-downloads" class="header-btn"><a href="./downloads.php">Downloads</a></li>
			<li id="header-btn-changelog" class="header-btn"><a href="./changelog.php">Changelog</a></li>
			<li id="header-btn-license" class="header-btn"><a href="./license.php">License</a></li>
		  </ul>
		  <a class="navbar-brand" id="the-logo" href="./index.php">
			<img src="img/logo.png" />
		  </a>
		  <ul class="nav navbar-nav">
			<li id="header-btn-credits" class="header-btn"><a href="./credits.php">Credits</a></li>
			<li id="header-btn-faq" class="header-btn"><a href="./faq.php">FAQ</a></li>
			<li id="header-btn" class="header-btn"><a href="http://vazkii.us">vazkii.us</a></li>
		  </ul>
		  </div>
		</div>
		
		<div class="container module-contents">
			<?php require_once "module/$module.php"; ?>
		</div>
		
		<footer class="footer">
			<div class="container text-muted">
				Psi (this website included) is licensed under the <a href="license.php">Psi License</a>.<br>
				Powered by <a href="http://fezvrasta.github.io/bootstrap-material-design/">Bootstrap Material Design</a> by <a href="https://github.com/FezVrasta">Fez Vrasta</a>.<br>
				Discuss Psi on the <a href="http://forum.feed-the-beast.com/threads/psi-discussion-thread.144282/">FTB Forums</a>.<br>				
				Misc Links: <a href="https://twitter.com/Vazkii">Twitter</a> <b>|</b> <a href="http://www.patreon.com/Vazkii">Patreon</a> <b>|</b> <a href="https://github.com/Vazkii/Psi">Github</a> <b>|</b> <a href="http://webchat.esper.net/?channels=vazkii">IRC</a><br><br>
				<i>I accept different opinions and objections, but this desire can't be fulfilled without carelessly using science, huh?</i> - <a href="https://www.youtube.com/watch?v=IzhMzY5avLI">LiSA</a>
			</div>
		</footer>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
		<script src="js/ripples.min.js"></script>
        <script src="js/material.min.js"></script>
		<script src="js/psi.js"></script>
		
		<?php echo "<script>\$(function(){var module='$module';var btn=\$('#header-btn-'+module);var txt=btn.text();btn.addClass('active');document.title+=(' - '+txt);});</script>"; ?>
	</body>
</html>
