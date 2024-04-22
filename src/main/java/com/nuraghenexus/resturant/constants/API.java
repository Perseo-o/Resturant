package com.nuraghenexus.resturant.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class API {

    /* *******************************  GENERIC  ***************************** */
    public static final String GEN_REQ_VERSION = "/api/v1/";
    public static final String GEN_MSG = "message";
    public static final String GEN_DATA = "data";
    public static final String GEN_TKN = "token";
    public static final String GEN_STATUS = "status";
    public static final String GEN_FOUND = "Trovato!";
    public static final String GEN_NOT_FOUND = "Non trovato!";
    public static final String GEN_FOUNDS = "Trovati!";
    public static final String GEN_NOT_FOUNDS = "Non trovati!";
    public static final String GEN_CRE_SUCCESS = "Creato con successo!";
    public static final String GEN_UPD_SUCCESS = "Aggiornato con successo!";
    public static final String GEN_DEL_SUCCESS = "Eliminato con successo!";
    public static final String GEN_DATA_NOT_EXISTS = "Il dato ricercato non esiste!";
    public static final String GEN_NOT_FOUND_CHECK = "on trovat";
    /* *********************************************************************** */




    /* ******************************  ABSTRACT  ****************************** */
    // CONTROLLER
    public static final String GET_ALL = "/getAll";
    public static final String CREATE = "/create";
    public static final String READ = "/read";
    public static final String READ_BY_UID = "/readByUserId";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String ID = "id";

    // SERVICE
    // DELETE
    public static final String DEL_NOT_DELETED = "Impossibile eliminare l'entità a causa di vincoli di integrità dei dati.";
    public static final String DEL_GEN_ERR = "Si è verificato un errore durante l'eliminazione dell'entità.";

    // FUNCTIONS
    /* *********************************************************************** */






    /* ********************************  USER  ******************************* */
    // CONTROLLER
    public static final String USER_REQ_MAP = API.GEN_REQ_VERSION + "user";

    // SERVICE
    public static final String USER_NOT_FOUND = "Utente non trovato";
    public static final String USER_REGEX_USRNM = "^[A-Z][a-zA-Z0-9]{4,14}$";
    public static final String USER_REGEX_PASS = "^[A-Z][a-zA-Z0-9]{7,24}$";

    /* *********************************************************************** */




    /* ***************************  AUTHENTICATION  ************************** */
    // CONTROLLER
    public static final String AUTH_REQ_MAP = API.GEN_REQ_VERSION + "auth";
    public static final String AUTH_REGISTER = "/signup";
    public static final String AUTH_AUTHENTICATE = "/signin";
    public static final String AUTH_DELETE = "/delete";

    //SERVICE
    public static final String AUTH_SUCCESSFUL_DELETE = "Eliminazione completata con successo.";
    public static final String AUTH_ERROR_DELETE = "Errore durante l'eliminazione: ";

    // SUCCESS
    public static final String AUTH_LOGGED_IN = "Ora sei loggato!";
    // ERRORS
    public static final String AUTH_EMAIL_CONFLICT = "Email già in uso, ritenta!";
    public static final String AUTH_INVALID_EMAIL = "Inserire una mail valida!";
    public static final String AUTH_SOMETHING_WENT_WRONG = "Qualcosa è andato storto!";
    public static final String AUTH_EMAIL_ERR = "Oops! Email errata, riprova!";
    public static final String AUTH_ACCOUNT_NOT_EXISTS = "Oops! Non esiste nessun account che corrisponda ai dati inseriti, riprova!";
    public static final String AUTH_PASS_ERR = "Oops! Password errata, riprova!";

    // FUNCTIONS
    public static final String AUTH_INVALID_PASSWORD = """
            Inserire una password valida!
            \nChe inizi con una lettera maiuscola .
            \nContenga almeno un numero.
            \nSia lunga da 8 a 25 caratteri.
            """;
    /* *********************************************************************** */






}
