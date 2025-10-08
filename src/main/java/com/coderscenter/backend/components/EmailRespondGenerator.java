package com.coderscenter.backend.components;

import com.coderscenter.backend.entities.profile.Employee;
import com.coderscenter.backend.entities.profile.User;
import com.coderscenter.backend.entities.schedule_management.Slot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailRespondGenerator {

    public String respondTextGenerator(User user, String password) {
        return """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 8px;">
                <div style="text-align: center;">
                    <img src="%s" alt="CODERS CENTER Logo" width="80"/>
                    <h2 style="color: #333;">Willkommen, %s!</h2>
                    <p>Danke für deine Registrierung bei <strong>Coders Center APP</strong>.</p>
                </div>
                <hr/>
                <p>Bitte ändere deine Daten.</p>
                <button><a href="http://localhost:5173">Hier</a></button>
                <p><strong>Name:</strong> %s</p>
                <p><strong>Email:</strong> %s</p>
                <p><strong>Passwort:</strong> %s</p>
                <hr/>
                <p style="font-size: 14px; color: gray;">
                    Falls du dich nicht registriert hast, ignoriere bitte diese E-Mail.
                </p>
            </div>
        </body>
        </html>
        """.formatted(
                "https://scontent.fvie1-1.fna.fbcdn.net/v/t39.30808-6/295364859_446580227477520_6687325563378273642_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=hj-EnoOelbMQ7kNvwE7QadM&_nc_oc=AdkOMrHLkRhvNj1hPKpgoKoZq_wqNaRWiAoXBeZbvMWKs-pJLRNqeIY1JMLpMGEjOEE&_nc_zt=23&_nc_ht=scontent.fvie1-1.fna&_nc_gid=pFJtu2He2w5lKYFd4m0axg&oh=00_AfZ7ErbqwliHSQ5LS1ZFobgsXBBY93iUOT4LW_CIFBaWkA&oe=68E191EB", // dein Logo oder Platzhalter
                user.getUsername(),
                user.getUsername(),
                user.getEmail(),
                password
        );
    }
    public String replacementTextGenerator(Slot slot, Employee employee) {
        return """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 8px;">
                <div style="text-align: center;">
                    <img src="%s" alt="CODERS CENTER Logo" width="80"/>
                    <h2 style="color: #333;">Willkommen, %s!</h2>
                    <p>Wir mussten den Einsatzplan anpassen bitte, kontrolliere deine Einsatze bei  <strong>Coders Center</strong>.</p>
                </div>
                <hr/>
                <p><strong>Name:</strong> %s</p>
                <p><strong>Gruppe:</strong> %s</p>
                <p><strong>Vertretung - Start:</strong> %s</p>
                <p><strong>Vertretung - Ende:</strong> %s</p>
                <hr/>
                <p style="font-size: 14px; color: gray;">
                    Danke fur dein Verständnis.
                </p>
            </div>
        </body>
        </html>
        """.formatted(
                "https://scontent.fvie1-1.fna.fbcdn.net/v/t39.30808-6/295364859_446580227477520_6687325563378273642_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=hj-EnoOelbMQ7kNvwE7QadM&_nc_oc=AdkOMrHLkRhvNj1hPKpgoKoZq_wqNaRWiAoXBeZbvMWKs-pJLRNqeIY1JMLpMGEjOEE&_nc_zt=23&_nc_ht=scontent.fvie1-1.fna&_nc_gid=pFJtu2He2w5lKYFd4m0axg&oh=00_AfZ7ErbqwliHSQ5LS1ZFobgsXBBY93iUOT4LW_CIFBaWkA&oe=68E191EB", // dein Logo oder Platzhalter
                employee.getFirstName() + " " + employee.getLastName(),
                employee.getFirstName() + " " + employee.getLastName(),
                slot.getDay().getWeek().getSchedule().getGroup().getName(),
                slot.getStartDate().toString(),
                slot.getEndDate().toString()
        );
    }

}

