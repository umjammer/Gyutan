// This software is a language translation version of "Open JTalk" developed by HTS Working Group.
// 
// Copyright (c) 2015-2016 Intelligent Communication Network (Ito-Nose) Laboratory
// Tohoku University
// Copyright (c) 2008-2016  Nagoya Institute of Technology
// Department of Computer Science
// 
// All rights reserved.
// 
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// * Redistributions of source code must retain the above copyright notice, 
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice, 
// this list of conditions and the following disclaimer in the documentation 
// and/or other materials provided with the distribution.
// * Neither the name of the "Intelligent Communication Network Laboratory, Tohoku University" nor the names of its contributors 
// may be used to endorse or promote products derived from this software 
// without specific prior written permission.
// 
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.icn.gyutan;

public class NJDAccentPhraseRule {

    static final String ACCENT_PHRASE_MEISHI = "名詞";
    static final String ACCENT_PHRASE_KEIYOUSHI = "形容詞";
    static final String ACCENT_PHRASE_DOUSHI = "動詞";
    static final String ACCENT_PHRASE_FUKUSHI = "副詞";
    static final String ACCENT_PHRASE_SETSUZOKUSHI = "接続詞";
    static final String ACCENT_PHRASE_RENTAISHI = "連体詞";
    static final String ACCENT_PHRASE_JODOUSHI = "助動詞";
    static final String ACCENT_PHRASE_JOSHI = "助詞";
    static final String ACCENT_PHRASE_KIGOU = "記号";
    static final String ACCENT_PHRASE_KEIYOUDOUSHI_GOKAN = "形容動詞語幹";
    static final String ACCENT_PHRASE_FUKUSHI_KANOU = "副詞可能";
    static final String ACCENT_PHRASE_SETSUBI = "接尾";
    static final String ACCENT_PHRASE_HIJIRITSU = "非自立";
    static final String ACCENT_PHRASE_RENYOU = "連用";
    static final String ACCENT_PHRASE_SETSUZOKUJOSHI = "接続助詞";
    static final String ACCENT_PHRASE_SAHEN_SETSUZOKU = "サ変接続";
    static final String ACCENT_PHRASE_TE = "て";
    static final String ACCENT_PHRASE_DE = "で";
    static final String ACCENT_PHRASE_SETTOUSHI = "接頭詞";
    static final String ACCENT_PHRASE_SEI = "姓";
    static final String ACCENT_PHRASE_MEI = "名";

	/*
	  Rule 01 デフォルトはくっつける
	  Rule 02 「名詞」の連続はくっつける
	  Rule 03 「形容詞」の後に「名詞」がきたら別のアクセント句に
	  Rule 04 「名詞,形容動詞語幹」の後に「名詞」がきたら別のアクセント句に
	  Rule 05 「動詞」の後に「形容詞」or「名詞」がきたら別のアクセント句に
	  Rule 06 「副詞」，「接続詞」，「連体詞」は単独のアクセント句に
	  Rule 07 「名詞,副詞可能」（すべて，など）は単独のアクセント句に
	  Rule 08 「助詞」or「助動詞」（付属語）は前にくっつける
	  Rule 09 「助詞」or「助動詞」（付属語）の後の「助詞」，「助動詞」以外（自立語）は別のアクセント句に
	  Rule 10 「*,接尾」の後の「名詞」は別のアクセント句に
	  Rule 11 「形容詞,非自立」は「動詞,連用*」or「形容詞,連用*」or「助詞,接続助詞,て」or「助詞,接続助詞,で」に接続する場合に前
	にくっつける
	  Rule 12 「動詞,非自立」は「動詞,連用*」or「名詞,サ変接続」に接続する場合に前にくっつける
	  Rule 13 「名詞」の後に「動詞」or「形容詞」or「名詞,形容動詞語幹」がきたら別のアクセント句に
	  Rule 14 「記号」は単独のアクセント句に
	  Rule 15 「接頭詞」は単独のアクセント句に
	  Rule 16 「*,*,*,姓」の後の「名詞」は別のアクセント句に
	  Rule 17 「名詞」の後の「*,*,*,名」は別のアクセント句に
	  Rule 18 「*,接尾」は前にくっつける
	*/

    static int strtopcmp(String str, String pattern) {
        char[] strat = str.toCharArray();
        char[] patat = pattern.toCharArray();

        for (int i = 0; ; i++) {
            if (i == pattern.length())
                return i;
            if (i == str.length())
                return -1;
            //if(str.charAt(i) != pattern.charAt(i))
            if (strat[i] != patat[i])
                return -1;
        }
    }

    static void set_accent_phrase(NJD njd) {
        if (njd == null || njd.head == null)
            return;

        for (NJDNode node = njd.head.next; node != null; node = node.next) {
            if (node.get_chain_flag() < 0) {
                rule_001(node);
                rule_002(node);
                rule_003(node);
                rule_004(node);
                rule_005(node);
                rule_006(node);
                rule_007(node);
                rule_008(node);
                rule_009(node);
                rule_010(node);
                rule_011(node);
                rule_012(node);
                rule_013(node);
                rule_014(node);
                rule_015(node);
                rule_016(node);
                rule_017(node);
                rule_018(node);
            }
        }
    }

    static void rule_001(NJDNode node) {
        node.set_chain_flag(1);
    }

    static void rule_002(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_MEISHI))
            if (node.get_pos().equals(ACCENT_PHRASE_MEISHI)) {
                node.set_chain_flag(1);
            }
    }

    static void rule_003(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_KEIYOUSHI))
            if (node.get_pos().equals(ACCENT_PHRASE_MEISHI)) {
                node.set_chain_flag(0);
            }
    }

    static void rule_004(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_MEISHI))
            if (node.prev.get_pos_group1().equals(ACCENT_PHRASE_KEIYOUDOUSHI_GOKAN))
                if (node.get_pos().equals(ACCENT_PHRASE_MEISHI)) {
                    node.set_chain_flag(0);
                }
    }

    static void rule_005(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_DOUSHI))
            if (node.get_pos().equals(ACCENT_PHRASE_KEIYOUSHI)) {
                node.set_chain_flag(0);
            } else if (node.get_pos().equals(ACCENT_PHRASE_MEISHI)) {
                node.set_chain_flag(0);
            }
    }

    static void rule_006(NJDNode node) {
        if (node.get_pos().equals(ACCENT_PHRASE_FUKUSHI)
                || node.prev.get_pos().equals(ACCENT_PHRASE_FUKUSHI)
                || node.get_pos().equals(ACCENT_PHRASE_SETSUZOKUSHI)
                || node.prev.get_pos().equals(ACCENT_PHRASE_SETSUZOKUSHI)
                || node.get_pos().equals(ACCENT_PHRASE_RENTAISHI)
                || node.prev.get_pos().equals(ACCENT_PHRASE_RENTAISHI)) {
            node.set_chain_flag(0);
        }
    }

    static void rule_007(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_MEISHI))
            if (node.prev.get_pos_group1().equals(ACCENT_PHRASE_FUKUSHI_KANOU)) {
                node.set_chain_flag(0);
            }

        if (node.get_pos().equals(ACCENT_PHRASE_MEISHI))
            if (node.get_pos_group1().equals(ACCENT_PHRASE_FUKUSHI_KANOU)) {
                node.set_chain_flag(0);
            }
    }

    static void rule_008(NJDNode node) {
        if (node.get_pos().equals(ACCENT_PHRASE_JODOUSHI)) {
            node.set_chain_flag(1);
        }
        if (node.get_pos().equals(ACCENT_PHRASE_JOSHI)) {
            node.set_chain_flag(1);
        }
    }

    static void rule_009(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_JODOUSHI))
            if (!node.get_pos().equals(ACCENT_PHRASE_JODOUSHI) &&
                    !node.get_pos().equals(ACCENT_PHRASE_JOSHI)) {
                node.set_chain_flag(0);
            }
        if (node.prev.get_pos().equals(ACCENT_PHRASE_JOSHI))
            if (!node.get_pos().equals(ACCENT_PHRASE_JODOUSHI) &&
                    !node.get_pos().equals(ACCENT_PHRASE_JOSHI)) {
                node.set_chain_flag(0);
            }
    }

    static void rule_010(NJDNode node) {
        if (node.prev.get_pos_group1().equals(ACCENT_PHRASE_SETSUBI))
            if (node.get_pos().equals(ACCENT_PHRASE_MEISHI)) {
                node.set_chain_flag(0);
            }
    }

    static void rule_011(NJDNode node) {
        if (node.get_pos().equals(ACCENT_PHRASE_KEIYOUSHI))
            if (node.get_pos_group1().equals(ACCENT_PHRASE_HIJIRITSU)) {
                if (node.prev.get_pos().equals(ACCENT_PHRASE_DOUSHI)) {
                    if (strtopcmp(node.prev.get_cform(), ACCENT_PHRASE_RENYOU) != -1) {
                        node.set_chain_flag(1);
                    }
                } else if (node.prev.get_pos().equals(ACCENT_PHRASE_KEIYOUSHI)) {
                    if (strtopcmp(node.prev.get_cform(), ACCENT_PHRASE_RENYOU) != -1) {
                        node.set_chain_flag(1);
                    }
                } else if (node.prev.get_pos().equals(ACCENT_PHRASE_JOSHI)) {
                    if (node.prev.get_pos_group1().equals(ACCENT_PHRASE_SETSUZOKUJOSHI)) {
                        if (node.prev.get_string().equals(ACCENT_PHRASE_TE)) {
                            node.set_chain_flag(1);
                        } else if (node.prev.get_string().equals(ACCENT_PHRASE_DE)) {
                            node.set_chain_flag(1);
                        }
                    }
                }
            }
    }

    static void rule_012(NJDNode node) {
        if (node.get_pos().equals(ACCENT_PHRASE_DOUSHI))
            if (node.get_pos_group1().equals(ACCENT_PHRASE_HIJIRITSU)) {
                if (node.prev.get_pos().equals(ACCENT_PHRASE_DOUSHI)) {
                    if (strtopcmp(node.prev.get_cform(), ACCENT_PHRASE_RENYOU) != -1) {
                        node.set_chain_flag(1);
                    }
                } else if (node.prev.get_pos().equals(ACCENT_PHRASE_MEISHI)) {
                    if (node.prev.get_pos_group1().equals(ACCENT_PHRASE_SAHEN_SETSUZOKU)) {
                        node.set_chain_flag(1);
                    }
                }
            }
    }

    static void rule_013(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_MEISHI)) {
            if (node.get_pos().equals(ACCENT_PHRASE_DOUSHI) ||
                    node.get_pos().equals(ACCENT_PHRASE_KEIYOUSHI) ||
                    node.get_pos_group1().equals(ACCENT_PHRASE_KEIYOUDOUSHI_GOKAN)) {
                node.set_chain_flag(0);
            }
        }
    }

    static void rule_014(NJDNode node) {
        if (node.get_pos().equals(ACCENT_PHRASE_KIGOU) ||
                node.prev.get_pos().equals(ACCENT_PHRASE_KIGOU)) {
            node.set_chain_flag(0);
        }
    }

    static void rule_015(NJDNode node) {
        if (node.get_pos_group3().equals(ACCENT_PHRASE_SETTOUSHI)) {
            node.set_chain_flag(0);
        }
    }

    static void rule_016(NJDNode node) {
        if (node.prev.get_pos_group3().equals(ACCENT_PHRASE_SEI)) {
            node.set_chain_flag(0);
        }
    }

    static void rule_017(NJDNode node) {
        if (node.prev.get_pos().equals(ACCENT_PHRASE_MEISHI) &&
                node.get_pos_group3().equals(ACCENT_PHRASE_MEI)) {
            node.set_chain_flag(0);
        }
    }

    static void rule_018(NJDNode node) {
        if (node.get_pos_group1().equals(ACCENT_PHRASE_SETSUBI)) {
            node.set_chain_flag(1);
        }
    }
}
