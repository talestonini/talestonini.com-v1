/***************************************************************************************************
 *
 * THIS CODE IS GENERATED AT COMPILE TIME BY LAIKA SBT PLUGIN.
 *
 * Do not modify it directly, as compilation will ovewrite your modifications.
 *
 **************************************************************************************************/
package com.talestonini.pages.sourcegen.posts

import com.talestonini.CodeSnippets
import com.talestonini.pages.BasePost
import scala.xml.Elem

object DockerVim extends BasePost {

  def postContent(): Elem =
    <div class="markdown-post-body">
      <p>In this post I would like to share my Scala and Java programming editor:
      <a href="https://github.com/talestonini/dockervim">dockervim</a>.  It&#39;s a dockerised <a href="https://neovim.io/">Neovim</a>
      running in an Ubuntu container, configured with some of the most common Vim plugins, like
      <a href="https://github.com/preservim/nerdtree">NERDTree</a> and <a href="https://github.com/airblade/vim-gitgutter">GitGutter</a>, the
      ability to search for text with the <a href="https://github.com/Numkil/ag.nvim">Silver Searcher</a>, fuzzy-search for files with
      <a href="https://github.com/kien/ctrlp.vim">Ctrl-P</a>, among many other plugins.</p>
      <p>But the coolest thing about it are actually the plugins to support programming in Scala and Java.  For <strong>Scala</strong>, it
      automates all the various configuration steps of <a href="https://scalameta.org/metals/docs/editors/vim.html">Metals for Vim</a>,
      giving it features like code completion (with the help of <a href="https://github.com/neoclide/coc.nvim">CoC</a> and
      <a href="https://github.com/scalameta/coc-metals">coc-metals</a>), jump to definition navigation, jump to code references, and hint
      on inferred types, to name a few.  For <strong>Java</strong> it combines <a href="https://github.com/neoclide/coc.nvim">CoC</a> and
      <a href="https://github.com/neoclide/coc-java">coc-java</a> to give it the same essential programming features.</p>
      <div class="aside">
        <img src="/img/vimide.png" alt="Programming in Scala"/>
        <figcaption>Fig.1 - Programming in Scala</figcaption>
      </div>
      <p>The biggest motivation behind this effort of automation was to have the ability to easily configure my tooling when
      changing work machines.  As a consultant, I&#39;m required to use client&#39;s laptops, which usually come in various different
      flavours of OS.  With this, all I need is Docker and I can get my machine ready to program in a few minutes.  You may
      then ask me: <em>Why not just use IntelliJ IDEA or VSCode with Metals (which by the way is a great alternative, and the one
      put forward first by the Scala Centre)?</em>  And I answer: <em>&quot;No Vim plugin feels the same as Vim or Neovim&quot;</em>.  I know this
      discussion can get to the realms of religion, I know.  I&#39;ll limit myself to saying that Vim is great and I encourage you
      to try it ;)</p>
      <p>You can pull the image from Docker Hub <a href="https://hub.docker.com/r/talestonini/dockervim">here</a>.  Just follow the few
      manual instructions on the page to install the pre-configured Vim plugins and a fix to the Java language server binary.
      Any feedback and improvement contributions are greatly appreciated. Thanks for reading.</p>
    </div>

}
