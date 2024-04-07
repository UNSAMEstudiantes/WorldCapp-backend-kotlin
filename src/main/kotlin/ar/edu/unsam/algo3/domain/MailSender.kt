package ar.edu.unsam.algo3.domain

interface MailSender {
    fun enviarMail(mail : Mail)
}

data class Mail(val de: String, val para: String, val asunto: String, val contenido: String)