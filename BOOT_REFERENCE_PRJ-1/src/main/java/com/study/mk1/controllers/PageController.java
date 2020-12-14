package com.study.mk1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

	@GetMapping("/ui/{fstPath}")
    public String uiPage(@PathVariable(value = "fstPath") String fstPath) {
        return fstPath;
    }

    @GetMapping("/ui/{fstPath}/{sndPath}")
    public String uiPage(@PathVariable(value = "fstPath") String fstPath, @PathVariable(value = "sndPath") String sndPath) {
        return fstPath + "/" + sndPath;
    }

    @GetMapping("/ui/{fstPath}/{sndPath}/{thirdPath}")
    public String uiPage(@PathVariable(value = "fstPath") String fstPath, @PathVariable(value = "sndPath") String sndPath, @PathVariable(value = "thirdPath") String thirdPath) {
        return fstPath + "/" + sndPath + "/" + thirdPath;
    }

    @GetMapping("/ui/{fstPath}/{sndPath}/{thirdPath}/{fourthPath")
    public String uiPage(@PathVariable(value = "fstPath") String fstPath, @PathVariable(value = "sndPath") String sndPath, @PathVariable(value = "thirdPath") String thirdPath, @PathVariable(value = "fourthPath") String fourthPath) {
        return fstPath + "/" + sndPath + "/" + thirdPath + "/" + fourthPath;
    }
    
}
