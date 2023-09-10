# TalesTonini.com

## Developing

In SBT:

    ~fastLinkJS

In another terminal:

    npm run dev

## Building

In SBT:

    ~fullLinkJS

In another terminal:

    npm run build      <-- this is what places artifacts in `dist`
    npm run preview

## TODO

### New features
- Update the about page
- Mastodon
- Likes
- ~~Tweet/LinkedIn a post~~
- Tags with cloud diagram?
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
