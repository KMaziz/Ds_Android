# Ds_Android
• pour le ds de le module android on a creé ce mini projet qui est une application de 'todo'.
<br/>• cette mini application permet aux utilisateurs d'ajouter et mettre à jour une tache('task') avec le titre, les détails et la date
<br/>• Toutes les données sont stockées dans une base de données en temps réel Firebase.
<br/>• Cette application 'Todo' utilise l'authentification Firebase pour l'enregistrement et la connexion des utilisateurs.
<br/>
<br/>
- - -
- - - 
### cette mini application contient :
 <br/>• 4 Activity
 <br/>• 2 fragments
 <br/>• Connexion avec une base de données Firebase pour sauvgarder les taches et les données des utlisateurs
 <br/>• connexion avec Firebase authentication pour le login
 <br/>• L'interface utilisateur est bien 'designed'
<br/>
<br/>
- - -
- - - 
## demo
&nbsp;&nbsp;
### Registration
&nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp;<img align="top" src="https://github.com/KMaziz/Ds_Android/blob/main/registration.gif" width="300">
&nbsp; &nbsp; &nbsp; &nbsp;tous les champs sont obligatoires pour l'inscription
<br/>
### add update & remove task
&nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp;<img align="top" src="https://github.com/KMaziz/Ds_Android/blob/main/ADD_update_task.gif" width="300">
&nbsp; &nbsp; &nbsp; &nbsp; seulement le champ time n'est pas obligatoire.
<br/>
### edit profile & logout
&nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp;<img align="top" src="https://github.com/KMaziz/Ds_Android/blob/main/profile.gif" width="300">
&nbsp; &nbsp; &nbsp; &nbsp;
<br/>le changement de mot de passe et de l'email necessite une re-authenticattion donc on doit ajouter des autres activités pour le faire et comme nous avons atteint notre objectif qui est de développer : au moins 3 activités, Au moins 1 fragment, Connexion avec une base de données Firebase et un design acceptable  et c'est un MINI-projet on a decidé de n'est pas le faire. <br/>** la modification de l'email se fait au niveau de la base de données Firebase en temps reel et non de l'authentification Firebase. **<br/>
## base de données
<br/><img align="top" src="https://github.com/KMaziz/Ds_Android/blob/main/db.PNG" width="300"><br/>le fichier google-services.json est inclus dans ce projet.<br/><br/>
## Design
<br/><img align="top" src="https://github.com/KMaziz/Ds_Android/blob/main/drawble.PNG" width="300">
<br/>pour creer un design personnalisé de notre buttons et edittext on a creé des fichiers drawable.
<br/>
<br/>
## FloatingActionButton
d'abord, on a définit tous les FloatingActionButton au meme endroit pour qu'ils se 'overlap'
<br/> puis on a define tous les FloatingActionButton en class HomeActivity.java
<br/> ensuite, on a utilisé animation (). TranslationY () pour animer les FloatingActionButton. pour qu'ils soient visible et overlap
<br/> enfin on a override la methode onBackPressed() pour qu'on puisse 'overlap' les FloatingActionButton on cliquant sur onBack
<br/>
## DatePicker
pour the datePicker nous avons suivi [ça] (https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext "stackoverflow")(c'est un lien pour question dans stackoverflow).
