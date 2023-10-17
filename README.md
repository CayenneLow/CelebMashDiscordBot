# CelebMash

This is a Discord bot that listens to the following commands:

```
!celeb "Displays posts from /r/celebs"
!celebnsfw "Displays posts from /r/celebnsfw
```

# Troubleshooting

## Set ENV file

Go to `src/resources/` and copy `.env.template` -> `.env` and substitute values with the below instructions

## Get Discord Bot Token

Go to [https://discord.com/developers/applications](https://discord.com/developers/applications) and select Celeb Mash. Go to "Bot" on the left sidebar and select "Reset Token"

## Get Reddit Refresh Token


### Detailed Instructions

Docs: https://github.com/reddit-archive/reddit/wiki/OAuth2#getting-started
Postman Collection: https://github.com/CayenneLow/CelebMashDiscordBot/blob/master/Reddit%20Auth.postman_collection.json

1. Make sure celebmash application exists here, otherwise create a new one: https://www.reddit.com/prefs/apps
2. Use Postman Collection, make first request in browser, replacing client id with the client in from the app in 1. (it is the string under `celebmash` under "developed applications")
3. After running this request, will be redirected to `http://google.com`. Look at the URL, it should now have a parameter called `code` DO NOT TAKE THE `#_` at the end.
4. Use the second Postman request and take the `code` from 3. and replace it in `code` under `Body` in Postman. Change Authorization: Username: Client ID from 2., Password: Secret from 2.
5. After making request from 4., will get `access_token` response, replace OAUTH_HEADER in .env with that token. replace REFRESH_TOKEN with the `refresh_token` in the response
6. Refer back to Postman request headers to get the encoded Basic Auth token, replace in REFRESH_AUTH_HEADER in `.env`