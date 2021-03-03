package dev.temnikov.gmail;

import com.pengrad.telegrambot.request.SendMessage;
import dev.temnikov.bots.MessageSender;
import dev.temnikov.domain.AppUser;
import dev.temnikov.domain.Payment;
import dev.temnikov.domain.enumeration.PaymentStatus;
import dev.temnikov.service.AppUserService;
import dev.temnikov.service.PaymentService;
import dev.temnikov.service.UserService;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewGmailMessageReader {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private MessageSender messageSender;

    public void main() throws Exception {
        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", 993, "paymentfortrash@gmail.com", "Safepassw0rd!");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        // Fetch unseen messages from inbox folder
        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        // Sort messages from recent to oldest
        Arrays.sort(
            messages,
            (m1, m2) -> {
                try {
                    return m2.getSentDate().compareTo(m1.getSentDate());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        );

        for (Message message : messages) {
            if (message.getSubject() != null && message.getSubject().contains("Ваш кошелек") && message.getSubject().contains("пополнен")) {
                System.out.println("sendDate: " + message.getSentDate() + " subject:" + message.getSubject());
                String content = (String) message.getContent();
                String[] split = content.split("<font color=\"#000000\" face=\"Arial\" size=\"3\">");
                Payment payment = createPayment(split);
                payment = paymentService.save(payment);
                AppUser user = payment.getUser();
                if (user != null) {
                    if (PaymentStatus.FAIL.equals(payment.getStatus())) {
                        messageSender.addMessageToClientBot(
                            new SendMessage(
                                user.getTelegramChatId(),
                                "Мы получили вашу оплату, но не смогли ее обработать, свяжитесь с поддержкой"
                            )
                        );
                    } else {
                        Long balance = user.getBalance();
                        balance += payment.getValue();
                        user.setBalance(balance);
                        user = userService.save(user);
                        messageSender.addMessageToClientBot(
                            new SendMessage(user.getTelegramChatId(), "Оплата зачислена. Ваш баланс = " + user.getBalance())
                        );
                    }
                }
                System.out.println(content);
            }
        }
        //inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);

    }

    private Payment createPayment(String[] split) {
        Payment payment = new Payment();
        String number = getPaymentNumber(split[4]);
        int sum = getPaymentValue(split[3]);
        String paymentInfo = getPaymentInfo(split[2]);
        payment.setPaymentDate(Instant.now());
        Optional<AppUser> byUserPhone = userService.findByUserPhone(number);
        if (byUserPhone.isEmpty()) {
            payment.setStatus(PaymentStatus.FAIL);
        } else {
            payment.setStatus(PaymentStatus.FAIL);
            if (sum != -1) {
                payment.setUser(byUserPhone.get());
            } else {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setValue(sum);
            }
        }
        return payment;
    }

    private String getPaymentInfo(String s) {
        return getStringBeforeFont(s);
    }

    private int getPaymentValue(String s) {
        String valueInfo = getStringBeforeFont(s);
        return parseValueBeforeCurrency(valueInfo);
    }

    private int parseValueBeforeCurrency(String valueInfo) {
        String[] valueCurrency = valueInfo.split(" ");
        if (valueCurrency.length < 2 || !valueCurrency[1].equals(" RUB")) {
            return -1;
        }
        try {
            return Integer.parseInt(valueCurrency[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    private String getPaymentNumber(String numberHtml) {
        return getStringBeforeFont(numberHtml);
    }

    private String getStringBeforeFont(String value) {
        String[] split = value.split("<");
        return split[0];
    }
}
