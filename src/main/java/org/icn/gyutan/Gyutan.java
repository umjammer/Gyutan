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

import java.io.FileOutputStream;
import java.io.PrintStream;

import net.java.sen.StringTagger;
import net.java.sen.Token;
import org.icn.sasakama.Engine;


public class Gyutan {
    private static final String OOV = "未知語";
    private static final String[] REPLACE = {
			"～", "〜",
			"\\.", "．",
			"%", "％",
			"\\+", "＋",
			"\\*", "＊",
			"-", "−",
			"/", "／",
			"=", "＝"
	};

    private StringTagger sen;
    private NJD njd;
    private JPCommon jpcommon;
    private Engine engine;

    public boolean initialize(String confPath, String fn_voice) {
        jpcommon = null;
        njd = null;

        // for Sen;
        boolean flagSen = initializeSen(confPath);

        // for HTS_Engine;
        boolean flagEngine = initializeEngine(fn_voice);

        return flagSen && flagEngine;
    }

    public boolean initializeSen(String confPath) {
        try {
            sen = StringTagger.getInstance(confPath + "/conf/sen.xml");
//            CompositPostProcessor cpp = new CompositPostProcessor();
//            cpp.readRules(new BufferedReader(new StringReader("記号-アルファベット")));
//            sen.addPostProcessor(cpp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            sen = null;
            return false;
        }
    }

    public boolean initializeEngine(String fn_voice) {
        String[] fn_voices = new String[1];
        fn_voices[0] = fn_voice;
        engine = new Engine();
        boolean flag = engine.load(fn_voices);

        if (!engine.get_full_context_label_format().equals("HTS_TTS_JPN")) {
            System.err.println("ERROR: Gyutan.initializeEngine(): hts_voice is not support HTS_TTS_JPN");
            engine.clear();
            engine = null;
            return false;
        }

        return flag;
    }

    public boolean availableSen() {
        return sen != null;
    }

    public boolean availableEngine() {
        return engine != null;
    }

    public String[] analysis_text(String text) {
        Token[] token = null;
        String[] feature = null;

        if (!availableSen())
            return null;

        try {
            token = sen.analyze(replace(hankakuToZenkaku(text)));
            feature = tokenToString(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return feature;
    }

    public String[] tokenToString(Token[] token) {
        String[] retString = new String[token.length];
        for (int i = 0; i < retString.length; i++) {
//            System.err.printf("surface:%s, terminfo:%s\n", token[i].getSurface(), token[i].getTermInfo());
//            System.err.printf("pos:%s,cform:%s,basic:%s,reading:%s,pron:%s\n",
//                    token[i].getPos(), token[i].getCform(), token[i].getBasicString(), token[i].getReading(), token[i].getPronunciation());
//            System.err.printf("addInfo:%s\n", token[i].getAddInfo());
            if (!token[i].getPos().equals(OOV)) {
//                String[] info = token[i].getTermInfo().split(",");
//                if (info[6].equals("自分"))
//                    info[8] = "ワタシ";
//
//                StringBuilder sb = new StringBuilder();
//                sb.append(info[0]);
//                for (int j = 1; j < info.length; j++) {
//                    sb.append(",");
//                    sb.append(info[j]);
//                }
//
//                retString[i] = token[i].getSurface() + "," + sb.toString();                    //System.err.printf("%s\n", retString[i]);
                retString[i] = token[i].getSurface() + "," + token[i].getTermInfo();
                if (!token[i].getPos().equals("記号-句点")) {
                    retString[i] += ",0/" + token[i].getPronunciation().length(); // TODO accent is 0 fixed
                }
            } else {
                retString[i] = String.format("%s,%s,*,*,*,*,*,%s,*,*,*,*,*", token[i].getSurface(), OOV, token[i].getSurface());
            }

//            System.err.printf("feature[%d]:%s\n", i, retString[i]);
        }

        return retString;
    }

    public void set_sampling_frequency(int sf) {
        engine.set_sampling_frequency(sf);
    }

    public void set_fperiod(int fp) {
        engine.set_fperiod(fp);
    }

    public void set_alpha(double a) {
        engine.set_alpha(a);
    }

    public void set_beta(double b) {
        engine.set_beta(b);
    }

    public void set_speed(double s) {
        engine.set_speed(s);
    }

    public void add_half_tone(double f) {
        engine.add_half_tone(f);
    }

    public void set_msd_threshould(int i, double f) {
        engine.set_msd_threshold(i, f);
    }

    public void set_gv_weight(int i, double f) {
        engine.set_gv_weight(i, f);
    }

    public void set_volume(double f) {
        engine.set_volume(f);
    }

    public void set_audio_buff_size(int i) {
        engine.set_audio_buff_size(i);
    }

    public String[] get_label(boolean withTime) {
        if (withTime)
            return engine.get_label();
        else
            return jpcommon.label.get_feature();
    }

    public int synthesis(String text, FileOutputStream wavf, FileOutputStream logf) {
        if (!availableSen() || !availableEngine())
            return -1;

        String[] feature = analysis_text(text);
        make_label(feature);

        return synthesis(wavf, logf);
    }

    public int synthesis(String[] feature, FileOutputStream wavf, FileOutputStream logf) {
        if (!availableEngine())
            return -1;

        make_label(feature);
        return synthesis(wavf, logf);
    }

    public int synthesis(String text) {
        return synthesis(text, null, null);
    }

    public int synthesis(FileOutputStream wavf, FileOutputStream logf) {
//        long t1, t2;
        if (jpcommon == null)
            return -1;
        int result = 0;

//        t1 = System.nanoTime();
        if (jpcommon.get_label_size() > 2) {
            if (engine.synthesize_from_strings(jpcommon.get_label_feature())) {
//                t2 = System.nanoTime();
//                System.err.printf("++synthesize time[us]:%f\n", (t2 - t1) / 1e+03);
                result = 1;
            }
            if (wavf != null)
                save_riff(wavf);
            if (logf != null) {
                save_information(logf);
            }
        }

        return result;
    }

    public boolean synthesis_from_fn(String labelFilename) {
        return engine.synthesize_from_fn(labelFilename);
    }

    public boolean synthesis_from_strings(String[] label) {
        return engine.synthesize_from_strings(label);
    }

    public void close_audio() {
        engine.close_audio();
    }

    public void save_information(FileOutputStream logf) {
        if (njd == null)
            return;

        njd.fprint(new PrintStream(logf));
        engine.save_information(logf);
    }

    public void save_label(FileOutputStream fos) {
        jpcommon.label.save_label(fos);
    }

    public void save_label(FileOutputStream fos, boolean withTime) {
        if (withTime)
            if (availableEngine())
                engine.save_label(fos);
            else
                save_label(fos);
    }

    public void save_riff(FileOutputStream wavf) {
        if (availableEngine())
            engine.save_riff(wavf);
    }

    public void make_label(String[] feature) {
//        long t1, t2, t3, t4, t5, t6, t7, t8;

//		t1 = System.nanoTime();
//        Token[] token = analysis_text(text);
//		t2 = System.nanoTime();
//		System.err.printf("++morpheme_analysis time[us]:%f\n", (t2-t1)/1e+03);

//        if (token == null)
//            return;

//        String[] feature = tokenToString(token);

//        t1 = System.nanoTime();
        njd = new NJD(feature);
//        njd.print();

//        t2 = System.nanoTime();
        NJDPronunciationRule.set_pronunciation(njd);
//        System.err.printf("== after set_pronunciation ==\n");
//        njd.print();

//        t3 = System.nanoTime();
        NJDDigitRule.set_digit(njd);
//        System.err.printf("== after set_digit ==\n");
//        njd.print();

//        t4 = System.nanoTime();
        NJDAccentPhraseRule.set_accent_phrase(njd);
//        System.err.printf("== after set_accent ==\n");
//        njd.print();

//        t5 = System.nanoTime();
        NJDAccentTypeRule.set_accent_type(njd);
//        System.err.printf("== after set_accent_type ==\n");
//        njd.print();

//        t6 = System.nanoTime();
        NJDUnvoicedVowelRule.set_unvoiced_vowel(njd);
//        System.err.printf("== after set_unvoiced_vowel ==\n");
//        njd.print();

//        t7 = System.nanoTime();
//        NJDLongVowelRule.set_long_vowel(njd);
//        System.err.printf("== after set_long_vowel==\n");
//        njd.print();
//        t8 = System.nanoTime();
//        System.err.printf("NJD              :%f\n", (t2 - t1) / 1e+03);
//        System.err.printf("PronunciationRule:%f\n", (t3 - t2) / 1e+03);
//        System.err.printf("DigitRule        :%f\n", (t4 - t3) / 1e+03);
//        System.err.printf("AccentPhraseRule :%f\n", (t5 - t4) / 1e+03);
//        System.err.printf("AccentTypeRule   :%f\n", (t6 - t5) / 1e+03);
//        System.err.printf("UnvoicedVowelRule:%f\n", (t7 - t6) / 1e+03);
//        System.err.printf("LongVowelRule    :%f\n", (t8 - t7) / 1e+03);
        jpcommon = new JPCommon(njd);
        jpcommon.make_label();

//        jpcommon.fprint(System.err);
//        t3 = System.nanoTime();
//        System.err.printf("++accent_analysis time[us]:%f\n", (t3 - t2) / 1e+03);
    }

    public String hankakuToZenkaku(String text) {
        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if ('0' <= c && c <= '9')
                sb.setCharAt(i, (char) (c - '0' + '０'));
            else if ('A' <= c && c <= 'Z')
                sb.setCharAt(i, (char) (c - 'A' + 'Ａ'));
            else if ('a' <= c && c <= 'z')
                sb.setCharAt(i, (char) (c - 'a' + 'ａ'));
        }

        return sb.toString();
    }

    public static String replace(String text) {
        for (int i = 0; i < REPLACE.length; i += 2) {
            text = text.replaceAll(REPLACE[i], REPLACE[i + 1]);
        }

        return text;
    }
}
