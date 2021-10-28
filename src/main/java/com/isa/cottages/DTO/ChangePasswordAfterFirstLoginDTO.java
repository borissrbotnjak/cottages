package com.isa.cottages.DTO;

import com.isa.cottages.fieldMatch.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "newPassword", second = "confirmNewPassword", message = "The password fields must match!")
public class ChangePasswordAfterFirstLoginDTO {

    private String newPassword;

    private String confirmNewPassword;
}
