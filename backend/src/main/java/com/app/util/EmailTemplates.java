package com.app.util;

public class EmailTemplates {

    private static final String CONFIRM_USER_URL = "http://localhost:3000/confirm-user/";

    public static String getConfirmationEmailTemplate(String token, String userName) {
        StringBuilder builder = new StringBuilder();

        builder
                .append("<!DOCTYPE html>")
                .append("<html lang=\"es\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Confirma tu cuenta en MediCal</title>")
                .append("</head>")
                .append("<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif; color: #333333; background-color: #f4f4f4;\">")
                .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"background: linear-gradient(to bottom right, #C084FC, #E879F9, #F472B6);\">")
                .append("<tr>")
                .append("<td align=\"center\" style=\"padding: 20px;\">")
                .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"600\" style=\"max-width: 600px; background-color: rgba(255, 255, 255, 0.9); border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\">")
                .append("<tr>")
                .append("<td style=\"position: relative;\">")
                .append("<img src=\"https://res.cloudinary.com/dpp28f2ek/image/upload/v1728075952/medico-mujeres-hospital-estetoscopio_23-2148827781_jjsz1b.avif\" alt=\"Banner de MediCal\" style=\"width: 100%; height: 150px; object-fit: cover; object-position: center; display: block;\">")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style=\"padding: 30px;\">")
                .append("<h2 style=\"color: #8B5CF6; font-size: 24px; margin-bottom: 20px; text-align: center;\">Confirma tu cuenta en MediCal</h2>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Estimado/a ").append(userName).append(",</p>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Gracias por registrarte en MediCal. Para completar tu registro y comenzar a utilizar nuestra plataforma, por favor confirma tu cuenta haciendo clic en el botón de abajo:</p>")
                .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">")
                .append("<tr>")
                .append("<td align=\"center\" style=\"padding: 30px 0;\">")
                .append("<a href=\"").append(CONFIRM_USER_URL).append(token).append("\" style=\"background-color: #8B5CF6; color: #ffffff; padding: 12px 24px; border-radius: 4px; text-decoration: none; font-weight: bold; display: inline-block;\">Confirmar mi cuenta</a>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Si no puedes hacer clic en el botón, copia y pega el siguiente enlace en tu navegador:</p>")
                .append("<p style=\"margin-bottom: 20px; word-break: break-all; color: #8B5CF6;\">").append(CONFIRM_USER_URL).append(token).append("</p>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Si no has creado una cuenta en MediCal, puedes ignorar este mensaje.</p>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Gracias por elegir MediCal para gestionar tus citas médicas.</p>")
                .append("<p style=\"line-height: 1.5;\">Saludos cordiales,<br>El equipo de MediCal</p>")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style=\"background-color: rgba(0, 0, 0, 0.1); padding: 20px; text-align: center; font-size: 12px; color: #666666;\">")
                .append("<p>Este es un mensaje automático, por favor no respondas a este correo.</p>")
                .append("<p>© 2023 MediCal. Todos los derechos reservados.</p>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</body>")
                .append("</html>");

        return builder.toString();
    }

    public static String getRecoveryPassEmailTemplate(String newPassword) {
        StringBuilder builder = new StringBuilder();

        builder
                .append("<!DOCTYPE html>")
                .append("<html lang=\"es\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Recupera tu contraseña en MediCal</title>")
                .append("</head>")
                .append("<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif; color: #333333; background-color: #f4f4f4;\">")
                .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"background: linear-gradient(to bottom right, #C084FC, #E879F9, #F472B6);\">")
                .append("<tr>")
                .append("<td align=\"center\" style=\"padding: 20px;\">")
                .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"600\" style=\"max-width: 600px; background-color: rgba(255, 255, 255, 0.9); border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\">")
                .append("<tr>")
                .append("<td style=\"position: relative;\">")
                .append("<img src=\"https://res.cloudinary.com/dpp28f2ek/image/upload/v1728075952/medico-mujeres-hospital-estetoscopio_23-2148827781_jjsz1b.avif\" alt=\"Banner de MediCal\" style=\"width: 100%; height: 150px; object-fit: cover; object-position: center; display: block;\">")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style=\"padding: 30px;\">")
                .append("<h2 style=\"color: #8B5CF6; font-size: 24px; margin-bottom: 20px; text-align: center;\">Recupera tu contraseña en MediCal</h2>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Estimado/a usuario/a,</p>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Hemos recibido una solicitud para restablecer tu contraseña. A continuación, te proporcionamos tu nueva contraseña:</p>")
                .append("<p style=\"margin-bottom: 20px; font-weight: bold; font-size: 18px; color: #8B5CF6; text-align: center;\">").append(newPassword).append("</p>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Una vez que inicies sesión, podrás cambiar esta contraseña en la sección Mi Perfil.</p>")
                .append("<p style=\"margin-bottom: 20px; line-height: 1.5;\">Si no has solicitado un restablecimiento de contraseña, por favor ignora este mensaje.</p>")
                .append("<p style=\"line-height: 1.5;\">Saludos cordiales,<br>El equipo de MediCal</p>")
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style=\"background-color: rgba(0, 0, 0, 0.1); padding: 20px; text-align: center; font-size: 12px; color: #666666;\">")
                .append("<p>Este es un mensaje automático, por favor no respondas a este correo.</p>")
                .append("<p>© 2023 MediCal. Todos los derechos reservados.</p>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</body>")
                .append("</html>");

        return builder.toString();
    }
}
