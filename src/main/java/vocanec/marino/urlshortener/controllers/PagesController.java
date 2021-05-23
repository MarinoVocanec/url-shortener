package vocanec.marino.urlshortener.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for serving HTML pages used for human interaction with this application.
 */
@Controller
public class PagesController {

    /**
     * Returns HTML view of the main frontend page used for testing API calls.
     * @return HTML main frontend page view.
     */
    @GetMapping("/")
    public String home() {
        return "home.html";
    }

    /**
     * Returns HTML view for the help page.
     * @return HTML help view.
     */
    @GetMapping("/help")
    public String help() {
        return "help.html";
    }
}
