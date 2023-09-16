# TalesTonini.com

## Developing

In SBT:

    ~fastLinkJS

In another terminal, execute `test_local.sh`, which essentially does:

    ./prep_deploy.sh public
    npm run dev

## Building

In SBT:

    ~fullLinkJS

In another terminal:

    npm run build      <-- this is what places artifacts in `dist`
    npm run preview

## Deploying

In SBT:

    fullLinkJS

In another terminal, execute `deploy.sh`, which essentially does:

    npm run build
    cp firebase.json dist
    firebase deploy --public dist

## TODO

### New features
- ~~Lazily build posts' `Element`s (improve performance loading images)~~
- Update the about page
- ~~Mastodon~~
- Likes
- ~~Tweet/LinkedIn a post~~
- Tags (with word cloud)
- ~~JS bundler~~: makes no sense, as the website does not depend on any npm library (that is not delivered by *Firebase
Hosting*)
- ~~Laika~~
  - ~~code with braces -> escape braces~~
- ~~Home content~~
- ~~Version number in footer~~

### Issues
- ~~Missing a page with terms and conditions / privacy policy~~
- ~~Improve the about page with sections about me and the website~~
- ~~Fix loading wheel when incognito~~
- ~~About page -> layout not good for desktop~~
- ~~About page with duplicate content when flipping mobile horizontally~~

### Nice to have
- ~~Open-source the website~~
- ~~Improve delivery of scripts/styles from `index.html` (Firebase ones are fine, I mean all others)~~
- Auto-deploy? with [Deploy to Firebase Hosting](https://github.com/marketplace/actions/deploy-to-firebase-hosting)
