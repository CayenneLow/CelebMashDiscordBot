# CelebMash

This is a Discord bot that listens to the following commands:

```
!celeb "Displays posts from /r/celebs"
!celebnsfw "Displays posts from /r/celebnsfw
```

# Troubleshooting

## Get Discord Bot Token

Go to [https://discord.com/developers/applications](https://discord.com/developers/applications) and select Celeb Mash. Go to "Bot" on the left sidebar and select "Reset Token"

## Get Reddit Refresh Token

### Postman Collection: [Postman Collection](https://github.com/CayenneLow/CelebMashDiscordBot/blob/master/Reddit%20Auth.postman_collection.json)

### Detailed Instructions

- Client_ID
CLIENT_ID is found here: [https://www.reddit.com/prefs/apps](https://www.reddit.com/prefs/apps) (the string under `celebmash` under "developed applications")

- Code
After running 1st requsest, will be redirected to `https://discordapp.com`. Look at the URL, it should now have a parameter called `code`.

- Basic Auth
Username: CLIENT_ID
Password: Secret, found in the same place as Client_ID (click `edit` on the app)

- AUTH_HEADER
This is the Basic Auth encoding, just copy from Postman since Postman will do the encoding for us. Find this under "Authorization"