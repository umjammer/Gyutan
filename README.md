[![Release](https://jitpack.io/v/umjammer/Gyutan.svg)](https://jitpack.io/#umjammer/Gyutan)
[![Java CI](https://github.com/umjammer/Gyutan/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/Gyutan/actions/workflows/maven.yml)
[![CodeQL](https://github.com/umjammer/Gyutan/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/Gyutan/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-8-b07219)
[![Parent](https://img.shields.io/badge/Parent-vavi--speech-pink)](https://github.com/umjammer/vavi-speech)

mavenized, librarinize Gyutan

## Install

 * clone this project
```shell
 $ cd $SRC_BASE
 $ git clone https://github.com/umjammer/Gyutan
```
 * download HTS voice
```shell
 $ cd $SRC_BASE
 $ git clone https://github.com/icn-lab/htsvoice-tohoku-f01.git
```
 * set "fn.voice" in local.properties
```shell
 $ cd $SRC_BASE/Gyutan
 $ vi local.properties
 $ cat local.properties
 fn.voice=/Users/nsano/src/java/htsvoice-tohoku-f01/tohoku-f01-neutral.htsvoice
```
 * install sen
```shell
 $ cd $SRC_BASE
 $ git clone https://gitlab.com/umjammer/sen.git
 $ cd $SRC_BASE/sen
 $ export SEN_HOME=$SRC_BASE/sen/src/main/home
 $ mvn install
```
 * set "sen.home" in local.properties
```shell
 $ cd $SRC_BASE/Gyutan
 $ vi local.properties
 $ cat local.properties
 fn.voice=/Users/nsano/src/java/htsvoice-tohoku-f01/tohoku-f01-neutral.htsvoice
 sen.home=/Users/nsano/src/java/sen/src/main/home
```

## Usage

```shell
 $ mvn test
```

## TODO

 * make njd as user friendly library

---
[Original](https://github.com/icn-lab/Gyutan)

# Gyutan
Copyright (c) 2015-2016 Intelligent Communication Network (Ito-Nose) Laboratory Tohoku University.   
Copyright (c) 2001-2016 Nagoya Institute of Technology Department of Computer Science.   
All rights reserved.  

Gyutan is a language translation version of "Open JTalk" developed by HTS Working Group.
Gyutan is a Japanese Text-to-Speech software implemented by Java.
Thanks to HTS Working Group, speech synthesis can be used easier.

## How to synthesize
Gyutan needs Sasakama(Sasakama.jar).
See also [Sasakama repository](https://github.com/icn-lab/Sasakama).

To synthesize speech from Japanese text, you must prepare speech model HMM (HTS voice) .

You can get HTS voice below.
* from our repository : <https://github.com/icn-lab/htsvoice-tohoku-f01>
* from HTS Working Group : <http://open-jtalk.sourceforge.net/>
* from MMDAgent Sample : <http://www.mmdagent.jp/>

Japanese morphological analyzer is needed to analyze Japanese text.
And to determine accent	type of	synthesized speech, accent dictionary is needed.
Sen, java implemented Japanese morphological analyzer, is used in this software.
And naist-jdic(ChaSen format) is used as dictionary.
Accent information is needed to synthesize speech.
Accent information is extracted from naist-jdic(MeCab format) in the Open JTalk package(1.08), and added to naist-jdic(ChaSen format)

## How to use
See bin/gyutan.sh(or bin/gyutan.bat on Windows).
You need to define shell variables of each JARs and dictionary.

### Link
* "Open JTalk" developed by HTS Working Group : <http://open-jtalk.sourceforge.net>
* Sen, Japanese morphological analyzer implement by Java : <https://java.net/projects/sen>
* naist-jdic (for chasen) : <https://osdn.jp/projects/naist-jdic/>

### What is Gyutan?
Gyutan is a cow's tongue.
Gyutan is a famous food in Sendai city, Japan (Our laboratory is in there).
