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

public class JPCommonLabelMora {
    String mora;
    JPCommonLabelPhoneme head;
    JPCommonLabelPhoneme tail;
    JPCommonLabelMora prev;
    JPCommonLabelMora next;
    JPCommonLabelWord up;

    void initialize(String mora, JPCommonLabelPhoneme head,
                    JPCommonLabelPhoneme tail, JPCommonLabelMora prev,
                    JPCommonLabelMora next, JPCommonLabelWord up) {
        this.mora = mora;
        this.head = head;
        this.tail = tail;
        this.prev = prev;
        this.next = next;
        this.up = up;
    }

    int index_mora_in_accent_phrase() {
        int i = 0;
        for (JPCommonLabelMora index = up.up.head.head; index != null; index = index.next) {
            i++;
            if (index == this)
                break;
        }
        return i;
    }

    int index_mora_in_breath_group() {
        int i = 0;
        for (JPCommonLabelMora index = up.up.up.head.head.head; index != null; index = index.next) {
            i++;
            if (index == this)
                break;
        }
        return i;
    }

    int index_mora_in_utterance() {
        int i = 0;
        for (JPCommonLabelMora index = this; index != null; index = index.prev)
            i++;
        return i;
    }

    int count_mora_in_accent_phrase() {
        int i = 0;
        for (JPCommonLabelMora index = up.up.head.head; index != null; index = index.next) {
            i++;
            if (index == up.up.tail.tail)
                break;
        }
        return i;
    }

    int count_mora_in_breath_group() {
        int i = 0;
        for (JPCommonLabelMora index = up.up.up.head.head.head; index != null; index = index.next) {
            i++;
            if (index == up.up.up.tail.tail.tail)
                break;
        }

        return i;
    }

    int count_mora_in_utterance() {
        int i = 0;
        for (JPCommonLabelMora index = this.next; index != null; index = index.next)
            i++;
        return index_mora_in_utterance() + i;
    }
}

