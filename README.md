# Spotyapp

## Comment faire fonctionner l'application

Tout d'abord vous devez disposer d'un compte spotify. <br>
Vous devez vous rendre sur ce lien: <https://developer.spotify.com/dashboard/login><br>
Une fois que vous êtes sur la page dashboard vous devez cliquer sur **CREATE AN APP** comme ci-dessous. <br>
![Create_App](https://cdn.discordapp.com/attachments/755346547549536296/823205714201018368/unknown.png)<br>
Maintenant vous devez remplir les informations relatives à l'application et accepeter les conditions. <br>
![Description](https://cdn.discordapp.com/attachments/755346547549536296/823206269996236800/unknown.png)<br>
Vous obtenez alors un Client ID qu'il faura rentrer directement dans le code de l'application, voici où se trouve le code et où le rentrer.<br>
![ID](https://cdn.discordapp.com/attachments/755346547549536296/823206715637760070/unknown.png)<br>
Entrez votre client ID ici dans le **MainActivity.java**.<br>
[Mainactivity](https://cdn.discordapp.com/attachments/755346547549536296/823208576351862844/unknown.png)<br>
Vous pouvez désormais aller dans **EDIT SETTINGS**.<br>
[Settings](https://cdn.discordapp.com/attachments/755346547549536296/823207760815980584/unknown.png)<br>
Avant de continuer, nous devons récupérer le SHA1 Fingerprint du projet. Pour cela suivez simplement ces screenshots.<br>
[Gradle](https://cdn.discordapp.com/attachments/755346547549536296/823209319805616198/unknown.png)<br>
[signingReport](https://cdn.discordapp.com/attachments/755346547549536296/823209468752953374/unknown.png)<br>
[SHA1](https://cdn.discordapp.com/attachments/755346547549536296/823209576572780544/unknown.png)<br>
Vous pouvez maintenant compléter les informations demander dans settings comme ci-dessous.<br>
[Infos](https://cdn.discordapp.com/attachments/755346547549536296/823210165914173451/unknown.png)<br>
Une fois que vous avez remplis les informations, vous pouvez lancer l'application, tout devrait fonctionner correctement.

## L'équipe:

### Anis Hannachi
### Gabriel Cavard