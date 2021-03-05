package dev.temnikov;

import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;

public class ApplicationUtils {

    private ApplicationUtils(){

    }


    public static String formatPhoneNumber(String input){
        if (StringUtils.isBlank(input)){
            return null;
        }
        CharSequence text = input;
        String country = "RU";
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        Iterator<PhoneNumberMatch> phoneNumberMatchIterator = util.findNumbers(text, country).iterator();
        if (!phoneNumberMatchIterator.hasNext()){
            return null;
        }
        PhoneNumberMatch m = phoneNumberMatchIterator.next();
        return "+7"+m.number().getNationalNumber();

    }

}
