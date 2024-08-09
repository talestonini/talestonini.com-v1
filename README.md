# TalesTonini.com

This is my personal website, where I add posts about varied stuff. It is developed in Scala.js and hosted with Firebase
Hosting.

## Pre-requisites

Attention with the Java version in use.  Java `17` and `20` have allowed SBT to launch fine, whereas `21` or `22` have
not until I updated SBT.  So make sure to have an SBT version that is compatible with your JDK in use (and make sure
that JDK is compatible with the Scala version of this project).

## Testing

Launch SBT with the provided script `sbt.sh` for more memory, and then test:

    test

**NOTE:** [`sbt-scoverage`](https://github.com/scoverage/sbt-scoverage) is not configured for this project as it only
supports ScalaJS 2 at the time of writing (8 Aug 2024).

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
- ~~Update the about page~~
- ~~Mastodon~~
- ~~Likes~~: not to do anymore, as I removed the login feature
- ~~Tweet/LinkedIn a post~~
- ~~Tags (with word cloud)~~
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
- ~~Auto-deploy? with [Deploy to Firebase Hosting](https://github.com/marketplace/actions/deploy-to-firebase-hosting)~~:
actually, configure deploy via GitHub Actions
