package com.bankbabu.balance.utils;

import com.bankbabu.balance.models.Bank;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankListSorter {

    private static final String[] priorityBanks = {"SBI", "BOB", "IDBI BANK", "HDFC BANK", "AXIS BANK",
            "Punjab National Bank", "ICICI BANK", "CANARA BANK", "BANK OF INDIA", "UNION BANK OF INDIA",
            "CENTRAL BANK OF INDIA", "ALLAHABAD BANK"};

    /**
     * According to requirements, lsit should be displayed in following order:
     * - Favourite banks
     * - Bank from the specified 'priority' list should be at the top.
     * Note that order in priority list also maters (one of the requirements,
     * that priority banks should be in specified order)
     * - All other bank ordered alphabetically
     *
     * @param suggestions unordered list of banks
     * @return ordered according to requirements banks
     */
    public static List<Bank> sortSuggestions(List<Bank> suggestions) {
        final List<Bank> sortedSuggestions = new ArrayList<>();

        // 1. Add All favourite banks
        for (Bank suggestion : suggestions) {
            if (suggestion.getFav() == 1) {
                sortedSuggestions.add(suggestion);
            }
        }

        // 2. Add banks from 'priority' list
        for (String bank : priorityBanks) {
            Bank foundBank = findBankByName(suggestions, bank);
            if (foundBank != null && foundBank.getFav() != 1) {
                sortedSuggestions.add(foundBank);
            }
        }

        // 3. Add all left banks, that wasn't added to the list
        ArrayList<Bank> alphabeticallySortedBanks = new ArrayList<>();
        for (Bank suggestion : suggestions) {
            if (suggestion.getFav() != 1 && !isPriorityBank(suggestion.getName())) {
                alphabeticallySortedBanks.add(suggestion);
            }
        }
        // Sort them alphabetically
        Collections.sort(alphabeticallySortedBanks,
                (bank1, bank2) -> bank1.getName().compareToIgnoreCase(bank2.getName()));

        // And add to the sorted result list
        sortedSuggestions.addAll(alphabeticallySortedBanks);

        return sortedSuggestions;
    }

    @Nullable
    private static Bank findBankByName(List<Bank> banks, String name) {
        for (Bank bank : banks) {
            if (name.equalsIgnoreCase(bank.getName())) {
                return bank;
            }
        }

        return null;
    }

    private static boolean isPriorityBank(String code) {
        for (String priorityBank : priorityBanks) {
            if (priorityBank.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }
}
