{%
  class.name = DockerVim
%}
In this post I would like to share my Scala and Java programming editor:
[dockervim](https://github.com/talestonini/dockervim).  It's a dockerised [Neovim](https://neovim.io/)
running in an Ubuntu container, configured with some of the most common Vim plugins, like
[NERDTree](https://github.com/preservim/nerdtree) and [GitGutter](https://github.com/airblade/vim-gitgutter), the
ability to search for text with the [Silver Searcher](https://github.com/Numkil/ag.nvim), fuzzy-search for files with
[Ctrl-P](https://github.com/kien/ctrlp.vim), among many other plugins.

But the coolest thing about it are actually the plugins to support programming in Scala and Java.  For **Scala**, it
automates all the various configuration steps of [Metals for Vim](https://scalameta.org/metals/docs/editors/vim.html),
giving it features like code completion (with the help of [CoC](https://github.com/neoclide/coc.nvim) and
[coc-metals](https://github.com/scalameta/coc-metals)), jump to definition navigation, jump to code references, and hint
on inferred types, to name a few.  For **Java** it combines [CoC](https://github.com/neoclide/coc.nvim) and
[coc-java](https://github.com/neoclide/coc-java) to give it the same essential programming features.

<div class="aside">
  <img src="/img/vimide.png" alt="Programming in Scala" />
  <figcaption>Fig.1 - Programming in Scala</figcaption>
</div>

The biggest motivation behind this effort of automation was to have the ability to easily configure my tooling when
changing work machines.  As a consultant, I'm required to use client's laptops, which usually come in various different
flavours of OS.  With this, all I need is Docker and I can get my machine ready to program in a few minutes.  You may
then ask me: *Why not just use IntelliJ IDEA or VSCode with Metals (which by the way is a great alternative, and the one
put forward first by the Scala Centre)?*  And I answer: *"No Vim plugin feels the same as Vim or Neovim"*.  I know this
discussion can get to the realms of religion, I know.  I'll limit myself to saying that Vim is great and I encourage you
to try it ;)

You can pull the image from Docker Hub [here](https://hub.docker.com/r/talestonini/dockervim).  Just follow the few
manual instructions on the page to install the pre-configured Vim plugins and a fix to the Java language server binary.
Any feedback and improvement contributions are greatly appreciated. Thanks for reading.
