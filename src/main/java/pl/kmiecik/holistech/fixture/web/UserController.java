package pl.kmiecik.holistech.fixture.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
class UserController {

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}