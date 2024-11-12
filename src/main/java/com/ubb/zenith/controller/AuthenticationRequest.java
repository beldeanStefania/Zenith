package com.ubb.zenith.controller;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
   private String username;
   private String password;
}
