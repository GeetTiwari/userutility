package com.utility.creation.utilitycreation.model.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private String status;
  private Date date;
  private String message;
  private String description;
}
