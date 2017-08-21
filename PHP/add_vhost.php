<?php

$server_dir = "../";

require $server_dir.'scripts/config.inc.php';
require $server_dir.'scripts/wampserver.lib.php';

// Définition de la langue des textes
if (isset ($_GET['lang'])) {
  $langue = htmlspecialchars($_GET['lang'],ENT_QUOTES);
  if ($langue != 'en' && $langue != 'fr' ) {
		$langue = 'en';
  }
}
elseif (isset ($_SERVER['HTTP_ACCEPT_LANGUAGE']) AND preg_match("/^fr/", $_SERVER['HTTP_ACCEPT_LANGUAGE'])) {
	$langue = 'fr';
}
else {
	$langue = 'en';
}
// Correction automatique des erreurs ?
$automatique = (isset($_POST['correct']) ? true : false);

// textes
$langues = array(
	'en' => array(
		'langue' => 'English',
		'locale' => 'english',
		'autreLangue' => 'Version Française',
		'autreLangueLien' => 'fr',
		'addVirtual' => 'Add a VirtualHost',
		'backHome' => 'Back to homepage',
		'VirtualSubMenuOn' => 'The <code>VirtualHost sub-menu</code> item must be set to (On) in the <code>Wamp Settings</code> Right-Click menu. Then reload this page',
		'UncommentInclude' => 'Uncomment <small>(Suppress #)</small> the line <code>#Include conf/extra/httpd-vhosts.conf</code><br>in file %s',
		'FileNotExists' => 'The file <code>%s</code> does not exists',
		'FileNotWritable' => 'The file <code>%s</code> is not writable',
		'DirNotExists' => '<code>%s</code> does not exists or is not a directory',
		'NotCleaned' => 'The <code>%s</code> file has not been cleaned.<br>There remain VirtualHost examples like: dummy-host.example.com',
		'NoVirtualHost' => 'There is no VirtualHost defined in <code>%s</code><br>It should at least have the VirtualHost for localhost.',
		'NoFirst' => 'The first VirtualHost must be <code>localhost</code> in <code>%s</code> file',
		'ServerNameInvalid' => 'The ServerName <code>%s</code> is invalid.',
		'VirtualHostName' => 'Name of the <code>Virtual Host</code> No diacritical characters (éçëñ) - No space - No underscore(_)',
		'VirtualHostFolder' => 'Complete absolute <code>path</code> of the VirtualHost <code>folder</code> <i>Examples: C:/wamp/www/projet/ or E:/www/site1/</i>',
		'VirtualAlreadyExist' => 'The ServerName <code>%s</code> already exists',
		'Start' => 'Start the creation of the VirtualHost (May take a while...)',
		'GreenErrors' => 'The green framed errors can be corrected automatically.',
		'Correct' => 'Start the automatic correction of errors inside the green borders panel',
		'NoModify' => 'Impossible to modify <code>httpd-vhosts.conf</code> or <code>hosts</code> files',
		'VirtualCreated' => 'The files have been modified. Virtual host <code>%s</code> was created',
		'CommandMessage' => 'Messages from the console to update DNS:',
		'However' => 'You may add another VirtualHost by validate "Add a VirtualHost".<br>However, for these new VirtualHost are taken into account by Apache, you must run item<br><code>Restart DNS</code><br>from Right-Click Tools menu of Wampmanager icon. <i>(This can unfortunately not be done automatically)</i>',
	),
	'fr' => array(
		'langue' => 'Français',
		'locale' => 'french',
		'autreLangue' => 'English Version',
		'autreLangueLien' => 'en',
		'addVirtual' => 'Ajouter un VirtualHost',
		'backHome' => 'Retour à l\'accueil',
		'VirtualSubMenuOn' => 'L\'item <code>Sous-menu VirtualHost</code> doit être validé dans le menu Clic-Droit <code>Paramètres Wamp</code><br>Validez cet item puis rechargez cette page',
		'UncommentInclude' => 'Décommenter <small>(Supprimer #)</small> la ligne <code>#Include conf/extra/httpd-vhosts.conf</code><br>dans le fichier %s',
		'FileNotExists' => 'Le fichier <code>%s</code> n\'existe pas',
		'FileNotWritable' => 'Le fichier <code>%s</code> est protégé en écriture.',
		'DirNotExists' => '<code>%s</code> n\'existe pas ou n\'est pas un dossier',
		'NotCleaned' => 'Le fichier <code>%s</code> n\'a pas été nettoyé.<br>Il reste des exemples de VirtualHost comme : dummy-host.example.com',
		'NoVirtualHost' => 'Aucun VirtualHost n\'est défini dans <code>%s</code><br>Il doit y avoir au moins un VirtualHost pour localhost.',
		'NoFirst' => 'Le premier VirtualHost doit être <code>localhost</code> dans le fichier <code>%s</code>',
		'ServerNameInvalid' => 'Le nom du ServerName <code>%s</code> n\'est pas valide.',
		'VirtualHostName' => 'Nom du <code>Virtual Host</code> Pas de caractères diacritiques (éçëñ) - Pas d\'espace - Pas de tiret bas (_)',
		'VirtualHostFolder' => '<code>Chemin</code> complet absolu du <code>dossier</code> VirtualHost - <i>Exemples : C:/wamp/www/projet/ ou E:/www/site1/</i>',
		'VirtualAlreadyExist' => 'Le ServerName <code>%s</code> existe déjà',
		'Start' => 'Démarrer la création du VirtualHost (Peut prendre un certain temps)',
		'GreenErrors' => 'Les erreurs encadrées en vert peuvent être corrigées automatiquement"',
		'Correct' => 'Démarrer la correction automatique des erreurs notées dans le cadre à bordures vertes',
		'NoModify' => 'Impossible de modifier les fichiers <code>httpd-vhosts.conf</code> ou <code>hosts</code>',
		'VirtualCreated' => 'Les fichiers ont été modifiés, le virtual host <code>%s</code> a été créé',
		'CommandMessage' => 'Messages de la console pour actualisation des DNS :',
		'However' => 'Vous pouvez ajouter un autre VirtualHost en validant "Ajouter un VirtualHost"<br>Cependant, pour que ces nouveaux VirtualHost soient pris en compte par Apache, vous devez lancer l\'item<br><code>Redémarrage DNS</code><br>du menu Outils par Clic-Droit sur l\'icône Wampmanager. <i>(Ceci ne peut, hélas, pas être fait automatiquement)</i>',
	)
);

$message_ok = '';
$message = array();
$errors = false;
$errors_auto = false;
$vhost_created = false;
$sub_menu_on = true;

//On récupère la valeur de VirtualHostMenu
$VirtualHostMenu = !empty($wampConf['VirtualHostSubMenu']) ? $wampConf['VirtualHostSubMenu'] : "off";
if($VirtualHostMenu !== "on") {
	$message[] = '<p class="warning">'.$langues[$langue]['VirtualSubMenuOn'].'</p>';
	$errors = true;
	$sub_menu_on = false;
}

/* Some tests about httpd-vhosts.conf file */
$virtualHost = check_virtualhost();
if($virtualHost['include_vhosts'] === false && !$errors) {
	if($automatique) {
		$httpConfFileContents = file_get_contents($c_apacheConfFile);
		$httpConfFileContents = preg_replace("~^[ \t]*#[ \t]*(Include[ \t]*conf/extra/httpd-vhosts.conf.*)$~m","$1",$httpConfFileContents,1);
		$fp = fopen($c_apacheConfFile,'w');
		fwrite($fp,$httpConfFileContents);
		fclose($fp);
		$virtualHost = check_virtualhost();
	}
	else {
		$message[] = '<p class="warning_auto">'.sprintf($langues[$langue]['UncommentInclude'],$c_apacheConfFile).'</p>';
		$errors = true;
		$errors_auto = true;
	}
}
if($virtualHost['vhosts_exist'] === false && !$errors) {
	if($automatique) {
		$fp = fopen($c_apacheVhostConfFile,'w');
		fclose($fp);
		$virtualHost = check_virtualhost();
	}
	else {
		$message[] = '<p class="warning_auto">'.sprintf($langues[$langue]['FileNotExists'],$c_apacheVhostConfFile).'</p>';
		$errors = true;
		$errors_auto = true;
	}
}
if(in_array("dummy", $virtualHost['ServerNameValid'], true) !== false && !$errors) {
	if($automatique) {
		$fp = fopen($c_apacheVhostConfFile,'w');
		fclose($fp);
		$virtualHost = check_virtualhost();
	}
	else {
		$message[] = '<p class="warning_auto">'.sprintf($langues[$langue]['NotCleaned'],$c_apacheVhostConfFile).'</p>';
		$errors = true;
		$errors_auto = true;
	}
}
if(empty($virtualHost['FirstServerName']) && !$errors) {
	if($automatique) {
		$virtual_localhost = <<< EOFLOCAL

<VirtualHost *:{$c_UsedPort}>
	ServerName localhost
	DocumentRoot {$wwwDir}
	<Directory  "{$wwwDir}/">
		Options Indexes FollowSymLinks MultiViews
		AllowOverride All
		Require local
	</Directory>
</VirtualHost>

EOFLOCAL;
		$fp = fopen($c_apacheVhostConfFile,'w');
		fwrite($fp,$virtual_localhost);
		fclose($fp);
		$virtualHost = check_virtualhost();

	}
	else {
		$message[] = '<p class="warning_auto">'.sprintf($langues[$langue]['NoVirtualHost'],$c_apacheVhostConfFile).'</p>';
		$errors = true;
		$errors_auto = true;
	}
}

/* If form submitted */
if (isset($_POST['submit']) && !$errors) {
	// Escape any backslashes used in the path to the file
	$c_apacheVhostConfFile_escape = str_replace('\\', '\\\\', $c_apacheVhostConfFile);
	$c_hostsFile_escape = str_replace('\\', '\\\\', $c_hostsFile);
	$vh_name = trim(strip_tags($_POST['vh_name']));
	$vh_folder = str_replace(array('\\','//'), '/',trim(strip_tags($_POST['vh_folder'])));
	if(substr($vh_folder,-1) == "/")
		$vh_folder = substr($vh_folder,0,-1);
	$vh_folder = strtolower($vh_folder);

	if($virtualHost['FirstServerName'] !== "localhost" && !$errors) {
		$message[] = '<p class="warning">'.sprintf($langues[$langue]['NoFirst'],$c_apacheVhostConfFile).'</p>';
		$errors = true;
	}
	/* Validité du nom de domaine */
	if(preg_match('/^
		[A-Za-z0-9]			# letter or number at the beginning
		(								# characters neither at the beginning nor at the end
			[-.](?![-.])	#  a . or - not followed by . or -
					|					#   or
			[A-Za-z0-9]		#  a letter or a number
		){1,60}					# this, repeated from 1 to 60 times
		[A-Za-z0-9]			# letter or number at the end
		$/x',$vh_name) == 0) {
		$message[] = '<p class="warning">'.sprintf($langues[$langue]['ServerNameInvalid'],$vh_name).'</p>';
		$errors = true;
	}
	if((!file_exists($vh_folder) || !is_dir($vh_folder)) && !$errors) {
		$message[] = '<p class="warning">'.sprintf($langues[$langue]['DirNotExists'],$vh_folder).'</p>';
		$errors = true;
	}
	if($c_hostsFile_writable !== true) {
		$message[] = '<p class="warning">'.sprintf($langues[$langue]['FileNotWritable'],$c_hostsFile).'</p>';
		$errors = true;
	}
	if(array_key_exists(strtolower($vh_name), array_change_key_case($virtualHost['ServerName'], CASE_LOWER))) {
		$message[] = '<p class="warning">'.sprintf($langues[$langue]['VirtualAlreadyExist'],$vh_name).'</p>';
		$errors = true;
	}
	if($errors === false) {
		/* Préparation du contenu des fichiers */
		$httpd_vhosts_add = <<< EOFNEWVHOST

<VirtualHost *:{$c_UsedPort}>
	ServerName {$vh_name}
	DocumentRoot {$vh_folder}
	<Directory  "{$vh_folder}/">
		Options Indexes FollowSymLinks MultiViews
		AllowOverride All
		Require local
	</Directory>
</VirtualHost>

EOFNEWVHOST;
		$hosts_add = <<< EOFHOSTS

127.0.0.1	{$vh_name}
::1	{$vh_name}

EOFHOSTS;
		/* Ouverture des fichiers pour ajout des lignes */
		$fp1 = fopen($c_apacheVhostConfFile_escape, 'a+');
		$fp2 = fopen($c_hostsFile_escape, 'a+');
		if (fwrite($fp1, $httpd_vhosts_add) && fwrite($fp2, $hosts_add)) {
			/* Actualisation des dns - Il faudrait redémarrer le service Apache par
			   	net stop wampapache
			   	net start wampapache
			   et c'est impossible car alors plus de PHP.
			   La commande ci-dessous fonctionne parfaitement dans un script comme wamp/script/msg.php
			   $command = 'start /b /wait '.$c_apacheExe.' -n wampapache -k restart';
			   mais pas si elle est lancée via http
			   et il n'existe pas de "graceful restart" Apache sous Windows*/

			$command = array(
				'ipconfig /flushdns',
				'net stop Dnscache',
				'net start Dnscache',
			);
			ob_start();
			foreach($command as $value) {
				echo "Command-> ".$value."\n";
				passthru($value);
			}
			$output = iconv("CP850","UTF-8//TRANSLIT", ob_get_contents());
			ob_end_clean();
			//$dns_refresh_message = '<pre><code>'.$output.'</code></pre>';
			$dns_refresh_message = "";

			$message_ok = '<p class="ok">'.sprintf($langues[$langue]['VirtualCreated'],$vh_name).'</p>';
			$message_ok .= '<h2>'.$langues[$langue]['CommandMessage'].'</h2>'.$dns_refresh_message;
			$message_ok .= '<p class="ok_plus">'.$langues[$langue]['However'].'</p>';
			$vhost_created = true;
		}
		else {
			$message = '<p class="warning">'.$langues[$langue]['NoModify'].'</p>';
		}
		fclose($fp1);
		fclose($fp2);
	}
}

$pageContents = <<< EOPAGE
<!DOCTYPE html>
<html lang="fr">
	<head>
		<title>Ajouter un "Virtual Host"</title>
		<meta charset="UTF-8">
		<style>
			* {
				margin: 0;
				padding: 0;
			}

			html {
				background: #ddd;
			}
			body {
				margin: 1em 10%;
				padding: 1em 3em;
				font: 80%/1.4 tahoma, arial, helvetica, lucida sans, sans-serif;
				border: 1px solid #999;
				background: #eee;
				position: relative;
			}
			header {
				margin-bottom: 1.8em;
				margin-top: .5em;
				padding-bottom: 0em;
				border-bottom: 1px solid #999;
				height: 125px;
				background: url(index.php?img=gifLogo) 0 0 no-repeat;
			}

			header h1 {
				padding-left: 130px;
				padding-top: 15px;
				font-size: 1.7em;
			}

			h2 {
				margin: 0.8em 0 0 0;
			}

			p {
				padding: 1%;
			}

			.ok, .ok_plus, .warning, .warning_auto {
				text-align: center;
				font-size: 1.3em;
				text-shadow: 1px 1px 0 #000;
				background: #585858;
			}

			.ok {
				color: limegreen;
			}
			.ok_plus {
				text-align:justify;
				background: #777777;
			}

			.warning, .warning_auto, .ok_plus {
				color: orange;
			}
			.warning_auto {
				border: 3px solid #4FEF10;
			}
			label {
				padding-left: 22px;
				margin-left: 22px;
				background: url(index.php?img=pngWrench) 0 100% no-repeat;
			}

			input[type="text"] {
				width: 96%;
				margin: 0.2% 1% 1% 1%;
				padding: 1%;
				border: 1px solid #999;
			}

			input[type="submit"] {
				min-width: 50%;
				background: #DDD;
				border: 1px solid #999;
				margin: 1%;
				padding: 1%;
			}

			input[type="submit"]:hover {
				background: #FF0099;
				color: #FFF;
			}

			pre {
				width: 98%;
				overflow: auto;
				padding: 1%;
				border: #FF0099 1px solid;
				background: #585858;
			}

			a {
				color: #000;
				text-decoration: none;
			}

			code {
				color: #FFF;
				text-shadow: 1px 1px 0 #000;
				padding: 0.1% 0.5%;
				border-radius: 3px;
				background: #585858;
				font-size: 1.2em;
			}
			.utility {
				position: absolute;
				right: 4em;
				top: 2em;
				font-size: 0.85em;
			}
		</style>
	</head>
	<body>
	<header>
		<h1><a href="add_vhost.php?lang={$langue}">{$langues[$langue]['addVirtual']}</a> - <a href="index.php?lang={$langue}">{$langues[$langue]['backHome']}</a></h1>
		<div class="utility"><a href='?lang={$langues[$langue]['autreLangueLien']}'>{$langues[$langue]['autreLangue']}</a>
    </div>
	</header>
EOPAGE;

if($vhost_created)
	$pageContents .= $message_ok;
else {
	$pageContents .= '<form method="post">';
	if($errors) {
		foreach($message as $value)
		 	$pageContents .= $value;
		}
	if($sub_menu_on === true) {
	$pageContents .= <<< EOPAGEB
		<p>Apache Virtual Hosts <code>{$c_apacheVhostConfFile}</code></p>
		<p>Windows hosts <code>{$c_hostsFile}</code></p>
EOPAGEB;
	if($errors_auto) {
	$pageContents .= <<< EOPAGEB
	<p><label>{$langues[$langue]['GreenErrors']}</label></p>
		<p style="text-align: right;"><input type="submit" name="correct" value="{$langues[$langue]['Correct']}" /></p>

EOPAGEB;
	}
	else {
	$pageContents .= <<< EOPAGEB
		<p><label>{$langues[$langue]['VirtualHostName']}</label><br>
			<input type="text" name="vh_name" required="required" /><br>
		<label>{$langues[$langue]['VirtualHostFolder']}</label><br>
			<input type="text" name="vh_folder" required="required"/></p>
		<p style="text-align: right;"><input type="submit" name="submit" value="{$langues[$langue]['Start']}" /></p>

EOPAGEB;
	}
	}
	$pageContents .= <<< EOPAGEB
	</form>
EOPAGEB;
}
$pageContents .= <<< EOPAGEB
</body>
</html>
EOPAGEB;

echo $pageContents;
?>