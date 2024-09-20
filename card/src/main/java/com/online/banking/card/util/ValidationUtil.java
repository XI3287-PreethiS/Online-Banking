package com.online.banking.card.util;

import com.online.banking.card.dto.CardRequestDto;
import com.online.banking.card.exception.InvalidCardException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ValidationUtil {

    public void validateCardRequest(CardRequestDto cardRequestDto) {
        if (cardRequestDto.getCvv().length() != 3) {
            throw new InvalidCardException(Constants.INVALID_CARD_DETAILS);
        }
        if (!StringUtils.hasText(cardRequestDto.getStakeholderName())) {
            throw new InvalidCardException(Constants.INVALID_NAME_DETAILS);
        }


    }
}
