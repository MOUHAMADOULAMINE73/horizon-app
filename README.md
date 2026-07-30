# Horizon — Guide 100% mobile (sans PC)

Tu vas faire tout ça depuis ton téléphone, avec juste un navigateur.
Aucun logiciel à installer sur un ordinateur. Tout est gratuit.

Deux étapes :
1. Mettre le backend (le serveur) en ligne sur **Render**
2. Compiler l'appli en fichier `.apk` via **GitHub**, puis l'installer

---

## ÉTAPE 1 — Créer un compte GitHub (2 min)

GitHub va héberger ton code et compiler l'appli pour toi automatiquement.

1. Va sur https://github.com depuis ton navigateur mobile
2. Crée un compte gratuit (email + mot de passe)

## ÉTAPE 2 — Mettre ton code sur GitHub

1. Une fois connecté, appuie sur le **+** en haut à droite → **New repository**
2. Nom du dépôt : `horizon-app`
3. Laisse-le en **Public** (plus simple pour la suite), puis **Create repository**
4. Sur la page du dépôt, appuie sur **Add file → Upload files**
5. Décompresse le fichier `horizon-android.zip` que je t'ai donné avec ton
   appli de gestion de fichiers (elle a normalement une option "Extraire"
   ou "Décompresser"), puis sélectionne **tout le contenu du dossier**
   `horizon-android` (pas le zip lui-même) et upload-le
   - Si ton navigateur te propose de choisir un dossier entier, choisis
     directement le dossier `horizon-android` — c'est plus simple
   - Sinon, uploade fichier par fichier en respectant les sous-dossiers
     (GitHub recrée l'arborescence automatiquement si tu glisses un dossier)
6. En bas de la page, appuie sur **Commit changes**

Fais la même chose pour le backend, dans un **deuxième dépôt** nommé
`horizon-backend`, avec le contenu du dossier `horizon-backend.zip`.

## ÉTAPE 3 — Déployer le backend sur Render (gratuit)

1. Va sur https://render.com et crée un compte gratuit
   (tu peux te connecter directement avec ton compte GitHub, c'est le plus simple)
2. Sur le tableau de bord, appuie sur **New → Web Service**
3. Connecte ton dépôt `horizon-backend` (Render va te demander
   l'autorisation d'accéder à ton GitHub — accepte)
4. Renseigne :
   - **Name** : `horizon-backend` (ou ce que tu veux)
   - **Runtime** : Python 3
   - **Build Command** : `pip install -r requirements.txt`
   - **Start Command** : `gunicorn app:app`
   - **Instance Type** : Free
5. Appuie sur **Create Web Service**
6. Attends 2-3 minutes que Render construise et démarre le serveur
7. Render te donne une URL du style :
   `https://horizon-backend-xxxx.onrender.com`
   👉 **Copie cette URL**, tu en as besoin à l'étape suivante

⚠️ Sur le plan gratuit, Render met le serveur "en veille" après 15 min
sans utilisation — la première requête après une pause peut prendre
30 secondes à répondre. Normal, pas un bug.

## ÉTAPE 4 — Connecter l'appli à ton serveur en ligne

1. Retourne sur ton dépôt `horizon-app` sur GitHub (celui de l'appli)
2. Ouvre le fichier :
   `app/src/main/java/com/horizon/app/network/RetrofitClient.kt`
3. Appuie sur l'icône crayon (✏️) pour éditer directement dans le navigateur
4. Remplace la ligne :
   ```
   private const val BASE_URL = "https://REMPLACE-MOI.onrender.com/"
   ```
   par ton URL Render réelle (avec le `/` à la fin), par exemple :
   ```
   private const val BASE_URL = "https://horizon-backend-xxxx.onrender.com/"
   ```
5. Fais pareil dans le fichier :
   `app/src/main/java/com/horizon/app/ui/screens/FeedScreen.kt`
   → remplace la ligne `val base = "https://REMPLACE-MOI.onrender.com"`
   (même URL, sans le `/` final cette fois)
6. En bas de page, **Commit changes** (choisis "Commit directly to the
   main branch")

## ÉTAPE 5 — Compiler l'APK automatiquement

Dès que tu fais un "Commit", GitHub compile l'appli tout seul.

1. Va dans l'onglet **Actions** en haut du dépôt `horizon-app`
2. Tu verras un build en cours (rond jaune/orange) — attends 3 à 5 minutes
   qu'il devienne vert ✅
3. Clique sur ce build terminé, puis en bas de la page tu verras
   **Artifacts → horizon-debug-apk**
4. Télécharge-le : c'est un fichier `.zip` contenant ton `app-debug.apk`
5. Décompresse-le avec ton appli de fichiers pour récupérer `app-debug.apk`

## ÉTAPE 6 — Installer l'appli sur ton téléphone

1. Ouvre le fichier `app-debug.apk` téléchargé (depuis ton dossier
   Téléchargements)
2. Android va te demander d'autoriser "l'installation d'applications
   inconnues" pour ton navigateur/gestionnaire de fichiers → accepte
   (c'est normal, c'est juste parce que l'appli ne vient pas du Play Store)
3. Appuie sur **Installer**

🎉 Horizon est maintenant installée sur ton téléphone, gratuitement,
sans PC, sans compte développeur payant.

---

## Si tu modifies le code plus tard

Répète juste les étapes 4 (si tu changes des fichiers) et 5-6 :
à chaque `Commit` sur GitHub, une nouvelle version de l'APK est
compilée automatiquement dans l'onglet **Actions**.

## Prochaines étapes possibles (V2)

- 💬 Messagerie complète entre utilisateurs (déjà prévue côté backend)
- 🎥 Upload et lecture de vidéos dans les annonces
- ⭐ Avis / notes entre utilisateurs
- 🔔 Notifications push
- 📍 Recherche géolocalisée

Dis-moi quand l'appli tourne chez toi et on passe à la suite.
