package uk.frontendlabs.nativedatachannels

import java.util.Arrays
import java.util.Random

/**
 * This class is used to generate random english pronounceable words
 * @author Maxime Roussy
 */
class WordGenerator
/**
 * Default, no argument constructor
 */
{
    //-----------------------------------------------------------------------------------------
    private val generator = Random()

    /**
     * Generates a new random, english pronounceable word
     * @param wordLength The desired length of the word to generate
     * @return A string containing the generated word
     * @throws IllegalArgumentException if the word length provided is less than 3
     */
    fun newWord(wordLength: Int): String {
        require(!(wordLength < 3)) { "Word lengths must be at least 3 characters long." }
        return generateRandomWord(wordLength)
    }

    private fun generateRandomWord(wordLength: Int): String {
        var randomWord: String
        randomWord = newWordStart
        while (randomWord.length != wordLength) {
            randomWord = addCharacter(wordLength, randomWord)
        }
        return randomWord
    }

    private val newWordStart: String
        private get() = START_BIGRAM.get(generateRandomIndex(START_BIGRAM.size))

    private fun generateRandomIndex(upperLimit: Int): Int {
        return generator.nextInt(upperLimit)
    }

    private fun addCharacter(wordLength: Int, currentWord: String): String {
        var currentWord = currentWord
        var mainIndex = getLookupIndex(currentWord)
        var type = getCharacterType(currentWord, wordLength)
        while (cannotProgress(mainIndex, type)) {
            currentWord = backtrack(currentWord)
            if (currentWord.length < 2) {
                currentWord = newWordStart
            }
            mainIndex = getLookupIndex(currentWord)
            type = getCharacterType(currentWord, wordLength)
        }
        return currentWord + getNextCharacter(mainIndex, type)
    }

    private fun getLookupIndex(currentWord: String): Int {
        val lookupCharacters = currentWord.substring(currentWord.length - 2)
        return Arrays.asList(*LOOKUP_BIGRAM).indexOf(lookupCharacters)
    }

    private fun getCharacterType(currentWord: String, wordLength: Int): Int {
        // Type refers to middle characters(0) or final characters(1)
        return if (currentWord.length == (wordLength - 1)) {
            1
        } else {
            0
        }
    }

    private fun cannotProgress(mainIndex: Int, type: Int): Boolean {
        return mainIndex < 0 || NEXT_CHAR_LOOKUP[mainIndex][type].size == 0
    }

    private fun backtrack(currentWord: String): String {
        return if (currentWord.length < 3) {
            ""
        } else {
            currentWord.substring(0, currentWord.length - 3)
        }
    }

    private fun getNextCharacter(mainIndex: Int, type: Int): String {
        val characterIndex = generateRandomIndex(NEXT_CHAR_LOOKUP[mainIndex][type].size)
        return NEXT_CHAR_LOOKUP[mainIndex][type][characterIndex]
    }

    companion object {
        //Bigram source and general concept based on https://github.com/scrollback/scrollback & described in https://www.hackerearth.com/notes/random-pronouncable-text-generator/
        private val START_BIGRAM = arrayOf(
            "TH",
            "OF",
            "AN",
            "IN",
            "TO",
            "CO",
            "BE",
            "HE",
            "RE",
            "HA",
            "WA",
            "FO",
            "WH",
            "MA",
            "WI",
            "ON",
            "HI",
            "PR",
            "ST",
            "NO",
            "IS",
            "IT",
            "SE",
            "WE",
            "AS",
            "CA",
            "DE",
            "SO",
            "MO",
            "SH",
            "DI",
            "AL",
            "AR",
            "LI",
            "WO",
            "FR",
            "PA",
            "ME",
            "AT",
            "SU",
            "BU",
            "SA",
            "FI",
            "NE",
            "CH",
            "PO",
            "HO",
            "DO",
            "OR",
            "UN",
            "LO",
            "EX",
            "BY",
            "FA",
            "LA",
            "LE",
            "PE",
            "MI",
            "SI",
            "YO",
            "TR",
            "BA",
            "GO",
            "BO",
            "GR",
            "TE",
            "EN",
            "OU",
            "RA",
            "AC",
            "FE",
            "PL",
            "CL",
            "SP",
            "BR",
            "EV",
            "TA",
            "DA",
            "AB",
            "TI",
            "RO",
            "MU",
            "EA",
            "NA",
            "SC",
            "AD",
            "GE",
            "YE",
            "AF",
            "AG",
            "UP",
            "AP",
            "DR",
            "US",
            "PU",
            "CE",
            "IF",
            "RI",
            "VI",
            "IM",
            "AM",
            "KN",
            "OP",
            "CR",
            "OT",
            "JU",
            "QU",
            "TW",
            "GA",
            "VA",
            "VE",
            "PI",
            "GI",
            "BI",
            "FL",
            "BL",
            "EL",
            "JO",
            "FU",
            "HU",
            "CU",
            "RU",
            "OV",
            "OB",
            "KE",
            "EF",
            "PH",
            "CI",
            "KI",
            "NI",
            "SL",
            "EM",
            "SM",
            "VO",
            "MR",
            "WR",
            "ES",
            "DU",
            "TU",
            "AU",
            "NU",
            "GU",
            "OW",
            "SY",
            "OC",
            "EC",
            "ED",
            "ID",
            "JE",
            "AI",
            "EI",
            "SK",
            "OL",
            "GL",
            "EQ",
            "LU",
            "AV",
            "SW",
            "AW",
            "EY",
            "TY"
        )
        private val LOOKUP_BIGRAM = arrayOf(
            "TH",
            "AN",
            "IN",
            "IO",
            "EN",
            "TI",
            "FO",
            "HE",
            "HA",
            "HI",
            "TE",
            "AT",
            "ER",
            "AL",
            "WA",
            "VE",
            "CO",
            "RE",
            "IT",
            "WI",
            "ME",
            "NC",
            "ON",
            "PR",
            "AR",
            "ES",
            "EV",
            "ST",
            "EA",
            "IV",
            "EC",
            "NO",
            "OU",
            "PE",
            "IL",
            "IS",
            "MA",
            "AV",
            "OM",
            "IC",
            "GH",
            "DE",
            "AI",
            "CT",
            "IG",
            "ID",
            " OR",
            "OV",
            "UL",
            "YO",
            "BU",
            "RA",
            "FR",
            "RO",
            "WH",
            "OT",
            "BL",
            "NT",
            "UN",
            "TR",
            "HO",
            "AC",
            "TU",
            "WE",
            "CA",
            "SH",
            "UR",
            "IE",
            "PA",
            "TO",
            "EE",
            "LI",
            "RI",
            "UG",
            "AM",
            "ND",
            "US",
            "LL",
            "AS",
            "TA",
            "LE",
            "MO",
            "WO",
            "MI",
            "AB",
            "EL",
            "IA",
            "NA",
            "SS",
            "AG",
            "TT",
            "NE",
            "PL",
            " LA",
            "OS",
            "CE",
            "DI",
            "BE",
            "AP",
            "SI",
            "NI",
            "OW",
            "SO",
            "AK",
            "CH",
            "EM",
            "IM",
            "SE",
            "NS",
            "PO",
            "EI",
            "EX",
            "KI",
            "UC",
            "AD",
            "GR",
            "IR",
            "NG",
            "OP",
            "SP",
            "OL",
            "DA",
            "NL",
            "TL",
            "LO",
            "BO",
            "RS",
            "FE",
            "FI",
            "SU",
            "GE",
            "MP",
            "UA",
            "OO",
            "RT",
            "SA",
            "CR",
            "FF",
            "IK",
            "MB",
            "KE",
            "FA",
            "CI",
            "EQ",
            "AF",
            "ET",
            "AY",
            "MU",
            "UE",
            "HR",
            "TW",
            "GI",
            "OI",
            "VI",
            "CU",
            "FU",
            "ED",
            "QU",
            "UT",
            "RC",
            "OF",
            "CL",
            "FT",
            "IZ",
            "PP",
            "RG",
            "DU",
            "RM",
            "YE",
            "RL",
            "DO",
            "AU",
            "EP",
            "BA",
            "JU",
            "RD",
            "RU",
            "OG",
            "BR",
            "EF",
            "KN",
            "LS",
            "GA",
            "PI",
            "YI",
            "BI",
            "IB",
            "UB",
            "VA",
            "OC",
            "IF",
            "RN",
            "RR",
            "SC",
            "TC",
            "CK",
            "DG",
            "DR",
            "MM",
            "NN",
            "OD",
            "RV",
            "UD",
            "XP",
            "JE",
            "UM",
            "EG",
            "DL",
            "PH",
            "SL",
            "GO",
            "CC",
            "LU",
            "OA",
            "PU",
            "UI",
            "YS",
            "ZA",
            "HU",
            "MR",
            "OE",
            "SY",
            "EO",
            "TY",
            "UP",
            "FL",
            "LM",
            "NF",
            "RP",
            "OH",
            "NU",
            "XA",
            "OB",
            "VO",
            "DM",
            "GN",
            " LD",
            "PT",
            "SK",
            "WR",
            "JO",
            "LT",
            "YT",
            "UF",
            "BJ",
            "DD",
            "EY",
            "GG",
            "GL",
            "GU",
            "HT",
            "LV",
            "MS",
            "NM",
            "NV",
            "OK",
            "PM",
            "RK",
            "SW",
            "TM",
            "XC",
            "ZE",
            "AW",
            "SM"
        )
        private val NEXT_CHAR_LOOKUP = arrayOf(
            arrayOf(arrayOf("E", "A", "I", "O", "R"), arrayOf("E", "O")),
            arrayOf(
                arrayOf("D", "T", "Y", "C", "S", "G", "N", "I", "O", "E", "A", "K"),
                arrayOf("D", "T", "Y", "S", "G", "O", "E", "A", "K")
            ),
            arrayOf(
                arrayOf("G", "T", "E", "D", "S", "C", "A", "I", "K", "V", "U", "N", "F"),
                arrayOf("G", "T", "E", "D", "S", "A", "K")
            ),
            arrayOf(arrayOf("N", "U", "R"), arrayOf("N", "U", "R")),
            arrayOf(
                arrayOf("T", "C", "D", "S", "E", "I", "G", "O", "N", "A"),
                arrayOf("T", "D", "S", "E", "G", "O", "A")
            ),
            arrayOf(
                arrayOf("O", "N", "C", "V", "M", "L", "E", "T", "S", "A", "R", "F"),
                arrayOf("N", "C", "M", "L", "E", "T", "S", "A", "R", "F")
            ),
            arrayOf(arrayOf("R", "U", "O", "L"), arrayOf("R", "U", "O", "L")),
            arrayOf(
                arrayOf("R", "N", "Y", "S", "M", "I", "A", "L", "D", "T"),
                arrayOf("R", "N", "Y", "S", "M", "A", "L", "D", "T")
            ),
            arrayOf(
                arrayOf("T", "D", "V", "N", "S", "R", "P", "L"),
                arrayOf("T", "D", "N", "S", "R", "L")
            ),
            arrayOf(
                arrayOf("S", "N", "C", "M", "L", "P", "G", "T", "R", "E"),
                arrayOf("S", "N", "C", "M", "L", "P", "G", "T", "R", "E")
            ),
            arrayOf(
                arrayOf("R", "D", "N", "S", "M", "L", "E", "C", "A"),
                arrayOf("R", "D", "N", "S", "M", "L", "E", "A")
            ),
            arrayOf(arrayOf("I", "E", "T", "H", "U", "O", "C"), arrayOf("E", "H", "O")),
            arrayOf(
                arrayOf("E", "S", "I", "A", "N", "Y", "T", "V", "M", "R", "O", "L", "G", "F", "C"),
                arrayOf("E", "S", "A", "N", "Y", "T", "M")
            ),
            arrayOf(
                arrayOf("L", "S", "I", "T", "E", "U", "O", "M", "K", "F", "A"),
                arrayOf("L", "S", "T", "E", "F")
            ),
            arrayOf(
                arrayOf("S", "Y", "R", "T", "N", "L"), arrayOf("S", "Y", "R", "T", "N", "L")
            ),
            arrayOf(arrayOf("R", "N", "L", "S", "D"), arrayOf("R", "N", "L", "S", "D")),
            arrayOf(
                arrayOf("N", "M", "U", "R", "L", "V", "S", "O"),
                arrayOf("N", "M", "U", "R", "L", "O")
            ),
            arrayOf(
                arrayOf("S", "A", "D", "N", "E", "C", "L", "T", "P", "M", "V", "G", "F", "Q"),
                arrayOf("S", "A", "D", "N", "E", "L", "T", "P", "M")
            ),
            arrayOf(
                arrayOf("H", "I", "Y", "E", "S", "T", "A", "U"), arrayOf("H", "Y", "E", "S", "A")
            ),
            arrayOf(arrayOf("T", "L", "N", "S"), arrayOf("T", "L", "N", "S")),
            arrayOf(
                arrayOf("N", "R", "D", "T", "S", "M", "A"),
                arrayOf("N", "R", "D", "T", "S", "M", "A")
            ),
            arrayOf(
                arrayOf("E", "I", "H", "T", "R", "O", "L"), arrayOf("E", "H", "T")
            ),
            arrayOf(
                arrayOf("S", "E", "T", "G", "A", "D", "L", "C", "V", "O", "I", "F"),
                arrayOf("S", "E", "T", "G", "A", "D", "O")
            ),
            arrayOf(
                arrayOf("O", "E", "I", "A"), arrayOf("E", "A")
            ),
            arrayOf(
                arrayOf(
                    "E",
                    "T",
                    "D",
                    "Y",
                    "S",
                    "I",
                    "R",
                    "L",
                    "M",
                    "K",
                    "G",
                    "A",
                    "O",
                    "N",
                    "C"
                ), arrayOf("E", "T", "D", "Y", "S", "M", "K", "A", "N")
            ),
            arrayOf(
                arrayOf("S", "T", "E", "I", "P", "U", "C"), arrayOf("S", "T", "E")
            ),
            arrayOf(arrayOf("E", "I"), arrayOf("E")),
            arrayOf(arrayOf("A", "R", "I", "E", "O", "U", "S"), arrayOf("A", "E", "O", "S")),
            arrayOf(
                arrayOf("R", "S", "T", "D", "L", "C", "N", "V", "M", "K"),
                arrayOf("R", "S", "T", "D", "L", "N", "M")
            ),
            arrayOf(arrayOf("E", "I", "A"), arrayOf("E")),
            arrayOf(arrayOf("T", "O", "I", "E", "A", "U", "R", "H"), arrayOf("T", "E", "H")),
            arrayOf(
                arrayOf("T", "W", "R", "U", "N", "M"), arrayOf("T", "W", "R", "U", "N", "M")
            ),
            arrayOf(
                arrayOf("T", "L", "R", "N", "S", "G", "P", "B"),
                arrayOf("T", "L", "R", "N", "S", "P")
            ),
            arrayOf(
                arrayOf("R", "N", "C", "A", "D", "T", "O"), arrayOf("R", "N", "A", "D", "T")
            ),
            arrayOf(arrayOf("L", "E", "I", "Y", "D", "A"), arrayOf("L", "E", "Y", "D")),
            arrayOf(
                arrayOf("T", "H", "S", "I", "E", "C", "M"), arrayOf("T", "H", "S", "E", "M")
            ),
            arrayOf(
                arrayOf("N", "T", "L", "K", "D", "S", "I", "G"),
                arrayOf("N", "T", "L", "D", "S")
            ),
            arrayOf(
                arrayOf("E", "I", "A"), arrayOf("E")
            ),
            arrayOf(arrayOf("E", "P", "M", "I", "A"), arrayOf("E")),
            arrayOf(
                arrayOf("A", "H", "E", "I", "T", "K", "U", "S"),
                arrayOf("H", "E", "T", "K", "S")
            ),
            arrayOf(
                arrayOf("T"), arrayOf("T")
            ),
            arrayOf(
                arrayOf("R", "N", "S", "D", "A", "V", "P", "T", "M", "L", "F"),
                arrayOf("R", "N", "S", "D", "A", "P", "T", "M", "L")
            ),
            arrayOf(
                arrayOf("N", "D", "R", "L", "T"), arrayOf("N", "D", "R", "L", "T")
            ),
            arrayOf(arrayOf("I", "E", "U", "S", "O"), arrayOf("E", "S", "O")),
            arrayOf(arrayOf("H", "N", "I"), arrayOf("H", "N")),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(
                arrayOf("E", "T", "M", "D", "S", "K", "I", "Y", "L", "G", "A", "R", "N", "C"),
                arrayOf("E", "T", "M", "D", "S", "K", "Y", "A", "N")
            ),
            arrayOf(
                arrayOf("E", "I"), arrayOf("E")
            ),
            arrayOf(arrayOf("D", "T", "A", "L"), arrayOf("D", "T", "L")),
            arrayOf(arrayOf("U"), arrayOf("U")),
            arrayOf(
                arrayOf("T", "S", "R", "I"), arrayOf("T", "S", "R")
            ),
            arrayOf(
                arrayOf("T", "N", "L", "C", "I", "M", "D", "S", "R", "P", "G", "B"),
                arrayOf("T", "N", "L", "M", "D", "S", "R")
            ),
            arrayOf(
                arrayOf("O", "E", "A"), arrayOf("E", "A")
            ),
            arrayOf(
                arrayOf(
                    "M",
                    "U",
                    "V",
                    "P",
                    "N",
                    "W",
                    "S",
                    "O",
                    "L",
                    "D",
                    "C",
                    "B",
                    "A",
                    "T",
                    "G"
                ), arrayOf("M", "U", "P", "N", "W", "O", "L", "D", "T")
            ),
            arrayOf(
                arrayOf("I", "E", "O", "A"), arrayOf("E", "O")
            ),
            arrayOf(arrayOf("H", "E", "T", "I"), arrayOf("H", "E")),
            arrayOf(arrayOf("E", "I", "Y", "O", "A"), arrayOf("E", "Y")),
            arrayOf(
                arrayOf("E", "I", "S", "R", "O", "A", "L", "Y", "U", "H"),
                arrayOf("E", "S", "O", "A", "Y", "H")
            ),
            arrayOf(arrayOf("D", "T", "I", "C", "G"), arrayOf("D", "T", "G")),
            arrayOf(arrayOf("A", "I", "O", "E", "U", "Y"), arrayOf("A", "E", "Y")),
            arrayOf(
                arrayOf("U", "W", "S", "R", "L", "O", "M", "T", "P", "N", "D"),
                arrayOf("U", "W", "R", "L", "O", "M", "T", "P", "N", "D")
            ),
            arrayOf(arrayOf("T", "E", "K", "H", "C", "R", "I"), arrayOf("T", "E", "K", "H")),
            arrayOf(
                arrayOf("R", "D", "A", "T"), arrayOf("R", "T")
            ),
            arrayOf(
                arrayOf("R", "L", "E", "V", "S", "N", "A"),
                arrayOf("R", "L", "E", "S", "N", "A")
            ),
            arrayOf(
                arrayOf("L", "N", "T", "R", "U", "S", "M", "P"),
                arrayOf("L", "N", "T", "R", "S", "M")
            ),
            arrayOf(arrayOf("E", "O", "I", "A"), arrayOf("E", "O")),
            arrayOf(
                arrayOf("E", "N", "T", "S", "I", "A", "Y", "R", "P", "C"),
                arrayOf("E", "N", "T", "S", "A", "Y")
            ),
            arrayOf(
                arrayOf("S", "N", "D", "T", "W", "V", "R", "L", "F"),
                arrayOf("S", "N", "D", "T", "W", "R", "L")
            ),
            arrayOf(arrayOf("R", "T", "S", "N", "L", "I", "C"), arrayOf("R", "T", "S", "N", "L")),
            arrayOf(
                arrayOf("R", "O", "N", "W", "P", "M", "L"),
                arrayOf("R", "O", "N", "W", "P", "M", "L")
            ),
            arrayOf(
                arrayOf("N", "D", "T", "M", "S", "R", "P", "L", "K"),
                arrayOf("N", "D", "T", "M", "S", "R", "P", "L", "K")
            ),
            arrayOf(
                arrayOf("N", "T", "S", "C", "K", "G", "E", "F", "Z", "V", "O", "M", "A"),
                arrayOf("N", "T", "S", "C", "G", "E", "F", "M", "A")
            ),
            arrayOf(
                arrayOf("N", "E", "C", "T", "S", "G", "A", "V", "O", "P", "M", "L", "D", "B"),
                arrayOf("N", "E", "C", "T", "S", "G", "A", "P", "M", "L", "D")
            ),
            arrayOf(
                arrayOf("H", "G"), arrayOf("H")
            ),
            arrayOf(arrayOf("E", "P", "I", "O", "A"), arrayOf("E")),
            arrayOf(arrayOf("E", "I", "S", "A", "U", "O"), arrayOf("E", "S", "O")),
            arrayOf(
                arrayOf("E", "T", "I", "S", "L", "H"), arrayOf("E", "T", "S", "H")
            ),
            arrayOf(arrayOf("Y", "E", "O", "I", "S", "A"), arrayOf("Y", "E", "S")),
            arrayOf(
                arrayOf("T", "S", "E", "I", "U", "O", "K", "H"), arrayOf("T", "S", "E", "O", "H")
            ),
            arrayOf(
                arrayOf("T", "N", "L", "I", "R", "K", "B", "G", "C"),
                arrayOf("T", "N", "L", "R")
            ),
            arrayOf(
                arrayOf("S", "D", "A", "T", "C", "R", "N", "M", "G", "V", "F"),
                arrayOf("S", "D", "A", "T", "R", "N", "M")
            ),
            arrayOf(arrayOf("R", "S", "V", "T", "U", "D"), arrayOf("R", "T", "U", "D")),
            arrayOf(
                arrayOf("R", "U"), arrayOf("R", "U")
            ),
            arrayOf(arrayOf("N", "L", "S", "T", "C", "G"), arrayOf("N", "L", "S", "T", "C", "G")),
            arrayOf(
                arrayOf("L", "O", "I"), arrayOf()
            ),
            arrayOf(
                arrayOf("L", "Y", "I", "E", "F", "O", "A", "T", "S", "P", "D"),
                arrayOf("L", "Y", "E", "F", "T", "S", "D")
            ),
            arrayOf(
                arrayOf("L", "N", "T"), arrayOf("L", "N", "T")
            ),
            arrayOf(arrayOf("L", "T", "R", "N", "M"), arrayOf("L", "T", "R", "N", "M")),
            arrayOf(
                arrayOf("I", "E", "U", "O", "A"), arrayOf("E", "O")
            ),
            arrayOf(arrayOf("E", "A", "O"), arrayOf("E", "O")),
            arrayOf(arrayOf("E", "L", "I"), arrayOf("E")),
            arrayOf(
                arrayOf("D", "S", "W", "R", "E", "Y", "V", "T", "L", "C", "A"),
                arrayOf("D", "S", "W", "R", "E", "Y", "T", "L", "A")
            ),
            arrayOf(arrayOf("A", "E", "I", "Y", "O"), arrayOf("E", "Y")),
            arrayOf(
                arrayOf("T", "N", "R", "S", "C", "Y", "W", "I", "B"),
                arrayOf("T", "N", "R", "S", "Y", "W")
            ),
            arrayOf(
                arrayOf("T", "E", "S", "I"), arrayOf("T", "E", "S")
            ),
            arrayOf(
                arrayOf("S", "N", "R", "D", "P", "L", "I"),
                arrayOf("S", "N", "R", "D", "P", "L")
            ),
            arrayOf(
                arrayOf("S", "N", "T", "D", "F", "E", "C", "A", "V", "R"),
                arrayOf("S", "N", "T", "D", "F", "E", "C", "A", "R")
            ),
            arrayOf(
                arrayOf("R", "E", "C", "T", "L", "F", "S", "I", "G", "D", "A"),
                arrayOf("R", "E", "T", "L", "S", "D", "A")
            ),
            arrayOf(
                arrayOf("P", "E", "A"), arrayOf("E")
            ),
            arrayOf(
                arrayOf("O", "N", "D", "T", "S", "G", "C", "B", "V", "M", "A"),
                arrayOf("N", "D", "T", "S", "G", "C", "M", "A")
            ),
            arrayOf(
                arrayOf("N", "T", "S", "C", "Z", "O", "G", "F"),
                arrayOf("N", "T", "S", "C", "G", "F")
            ),
            arrayOf(arrayOf("N", "E", "S", "I", "A"), arrayOf("N", "E", "S")),
            arrayOf(arrayOf("N", "M", "U", "L", "C", "R"), arrayOf("N", "M", "U", "L", "R")),
            arrayOf(
                arrayOf("E", "I"), arrayOf("E")
            ),
            arrayOf(arrayOf("E", "A", "I", "O", "U", "R"), arrayOf("E", "O")),
            arrayOf(arrayOf("E", "S", "P", "O", "B", "A", "I"), arrayOf("E", "S")),
            arrayOf(
                arrayOf("E", "P", "I", "A", "S", "M"), arrayOf("E", "S")
            ),
            arrayOf(
                arrayOf("D", "N", "L", "S", "R", "E", "C", "T", "V", "A"),
                arrayOf("D", "N", "L", "S", "R", "E", "T", "A")
            ),
            arrayOf(
                arrayOf("T", "I", "E"), arrayOf("T", "E")
            ),
            arrayOf(arrayOf("S", "R", "N", "L", "W", "T", "I"), arrayOf("R", "N", "L", "W", "T")),
            arrayOf(
                arrayOf("R", "N", "G", "T"), arrayOf("R", "N", "G", "T")
            ),
            arrayOf(arrayOf("P", "T", "I", "C", "A"), arrayOf("T")),
            arrayOf(arrayOf("N"), arrayOf("N")),
            arrayOf(
                arrayOf("H", "T", "K", "E"), arrayOf("H", "T", "K", "E")
            ),
            arrayOf(arrayOf("E", "I", "Y", "V", "M", "D"), arrayOf("E", "Y")),
            arrayOf(arrayOf("E", "A", "O"), arrayOf("E", "A")),
            arrayOf(
                arrayOf("E", "S", "T", "L", "I"), arrayOf("E", "S", "T")
            ),
            arrayOf(arrayOf("E", "S", "L", "T", "R", "I"), arrayOf("E", "S")),
            arrayOf(arrayOf("E", "P", "L"), arrayOf("E")),
            arrayOf(
                arrayOf("E", "O", "I", "A"), arrayOf("E")
            ),
            arrayOf(arrayOf("D", "L", "I", "O", "E", "U"), arrayOf("D", "L", "E")),
            arrayOf(
                arrayOf("Y", "T", "R", "N"), arrayOf("Y", "T", "R", "N")
            ),
            arrayOf(arrayOf("Y"), arrayOf("Y")),
            arrayOf(arrayOf("Y", "E"), arrayOf("Y", "E")),
            arrayOf(
                arrayOf("W", "N", "O", "S", "C", "V", "U", "T", "R", "P", "G"),
                arrayOf("W", "N", "O", "U", "T", "R", "P")
            ),
            arrayOf(arrayOf("U", "T", "R", "O", "D", "A"), arrayOf("U", "T", "R", "O", "D")),
            arrayOf(
                arrayOf("T", "E", "O", "I"), arrayOf("T", "E", "O")
            ),
            arrayOf(arrayOf("R", "E", "W", "L", "C", "A"), arrayOf("R", "E", "W", "L", "A")),
            arrayOf(
                arrayOf("R", "N", "C", "E", "L", "G"), arrayOf("R", "N", "C", "E", "L", "G")
            ),
            arrayOf(arrayOf("R", "C", "P", "B", "M", "L", "A"), arrayOf("R", "P", "M", "L")),
            arrayOf(
                arrayOf("N", "T", "S", "R", "D"), arrayOf("N", "T", "S", "R", "D")
            ),
            arrayOf(arrayOf("L", "O", "A", "T", "R", "E"), arrayOf("T", "E")),
            arrayOf(arrayOf("L", "T", "R"), arrayOf("L", "T", "R")),
            arrayOf(
                arrayOf("K", "D", "L", "T", "R", "N", "M"),
                arrayOf("K", "D", "L", "T", "R", "N", "M")
            ),
            arrayOf(arrayOf("I", "H", "A", "E", "Y", "U", "S"), arrayOf("H", "A", "E", "Y", "S")),
            arrayOf(
                arrayOf("I", "M", "Y", "N", "L"), arrayOf("M", "Y", "N", "L")
            ),
            arrayOf(arrayOf("E", "I", "O", "A"), arrayOf("E", "A")),
            arrayOf(arrayOf("E", "I"), arrayOf("E")),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(arrayOf("D", "N", "T", "S", "R", "E"), arrayOf("D", "N", "T", "S", "R", "E")),
            arrayOf(
                arrayOf("C", "R", "M", "I"), arrayOf("R", "M")
            ),
            arrayOf(arrayOf("A", "T", "E", "S", "P", "N"), arrayOf("A", "T", "E", "S", "P", "N")),
            arrayOf(
                arrayOf("U"), arrayOf()
            ),
            arrayOf(arrayOf("T", "F"), arrayOf("T", "F")),
            arrayOf(
                arrayOf("T", "I", "H", "E", "Y", "W", "S", "A"),
                arrayOf("H", "E", "Y", "S", "A")
            ),
            arrayOf(
                arrayOf("S", "E"), arrayOf("S")
            ),
            arrayOf(arrayOf("S", "N", "L", "C"), arrayOf("S", "N", "L")),
            arrayOf(arrayOf("S", "N"), arrayOf("S", "N")),
            arrayOf(
                arrayOf("O", "E", "I"), arrayOf("E")
            ),
            arrayOf(arrayOf("O", "E"), arrayOf("O", "E")),
            arrayOf(arrayOf("N", "V", "O", "C"), arrayOf("N", "C")),
            arrayOf(
                arrayOf("N"), arrayOf("N")
            ),
            arrayOf(
                arrayOf("N", "D", "S", "C", "T", "O", "L", "E"),
                arrayOf("N", "D", "S", "C", "T", "L", "E")
            ),
            arrayOf(
                arrayOf("L", "R", "T", "S"), arrayOf("L", "R", "T", "S")
            ),
            arrayOf(arrayOf("L", "R", "N"), arrayOf("L", "R", "N")),
            arrayOf(arrayOf("I", "U", "E"), arrayOf("E")),
            arrayOf(
                arrayOf("I", "E", "A"), arrayOf("E")
            ),
            arrayOf(arrayOf("I", "H", "E"), arrayOf("H", "E")),
            arrayOf(arrayOf("H", "E"), arrayOf("H", "E")),
            arrayOf(
                arrayOf("F", "T"), arrayOf("F", "T")
            ),
            arrayOf(arrayOf("E", "A", "U", "O"), arrayOf("E")),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(
                arrayOf("E", "A"), arrayOf("E")
            ),
            arrayOf(arrayOf("E", "O", "R", "L"), arrayOf("E")),
            arrayOf(arrayOf("E", "A"), arrayOf("E")),
            arrayOf(
                arrayOf("C", "S", "R", "A"), arrayOf("S", "R")
            ),
            arrayOf(arrayOf("A", "S", "I", "E"), arrayOf("S", "E")),
            arrayOf(arrayOf("A", "S", "D"), arrayOf("A", "S", "D")),
            arrayOf(
                arrayOf("Y", "D"), arrayOf("Y", "D")
            ),
            arrayOf(arrayOf("W", "N", "M", "E"), arrayOf("W", "N", "M")),
            arrayOf(arrayOf("T", "S"), arrayOf("T", "S")),
            arrayOf(
                arrayOf("T", "O", "E", "A"), arrayOf("T", "E")
            ),
            arrayOf(arrayOf("S", "C", "R", "N", "L"), arrayOf("S", "R", "N", "L")),
            arrayOf(
                arrayOf("S"), arrayOf("S")
            ),
            arrayOf(arrayOf("S", "E", "I"), arrayOf("S", "E")),
            arrayOf(arrayOf("S", "N", "C"), arrayOf("S", "N")),
            arrayOf(
                arrayOf("R", "I"), arrayOf()
            ),
            arrayOf(arrayOf("O", "I", "E", "A"), arrayOf("E", "A")),
            arrayOf(arrayOf("O", "F", "U", "T", "E"), arrayOf("F", "T", "E")),
            arrayOf(
                arrayOf("O", "E"), arrayOf("O", "E")
            ),
            arrayOf(arrayOf("O"), arrayOf("O")),
            arrayOf(arrayOf("N", "I", "T", "R"), arrayOf("N", "T", "R")),
            arrayOf(
                arrayOf("N", "T", "R", "E", "C"), arrayOf("N", "T", "R", "E", "C")
            ),
            arrayOf(arrayOf("N"), arrayOf("N")),
            arrayOf(arrayOf("L", "T", "N"), arrayOf("L", "T", "N")),
            arrayOf(
                arrayOf("L", "I", "E"), arrayOf("E")
            ),
            arrayOf(arrayOf("L"), arrayOf()),
            arrayOf(arrayOf("L", "T", "R", "N"), arrayOf("L", "T", "R", "N")),
            arrayOf(
                arrayOf("K", "I", "E", "C", "A"), arrayOf("K", "E")
            ),
            arrayOf(arrayOf("I", "F", "E", "T"), arrayOf("F", "E", "T")),
            arrayOf(arrayOf("I", "E", "M", "A"), arrayOf("E", "A")),
            arrayOf(
                arrayOf("I", "E", "Y", "O"), arrayOf("E", "Y")
            ),
            arrayOf(arrayOf("H", "R", "O", "I", "A"), arrayOf("H")),
            arrayOf(arrayOf("H"), arrayOf("H")),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(arrayOf("E", "O", "I", "A"), arrayOf("E", "A")),
            arrayOf(
                arrayOf("E", "U", "I"), arrayOf("E")
            ),
            arrayOf(arrayOf("E", "O", "I"), arrayOf("E", "O")),
            arrayOf(arrayOf("E", "Y", "U"), arrayOf("E", "Y")),
            arrayOf(
                arrayOf("E", "I"), arrayOf("E")
            ),
            arrayOf(arrayOf("E", "I"), arrayOf("E")),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(
                arrayOf("C"), arrayOf()
            ),
            arrayOf(arrayOf("B", "E"), arrayOf("E")),
            arrayOf(arrayOf("A", "R", "I", "E"), arrayOf("E")),
            arrayOf(
                arrayOf("Y", "E"), arrayOf("Y", "E")
            ),
            arrayOf(arrayOf("Y", "O", "I", "E"), arrayOf("Y", "O", "E")),
            arrayOf(arrayOf("Y", "A"), arrayOf("Y")),
            arrayOf(
                arrayOf("V", "T", "O"), arrayOf("T", "O")
            ),
            arrayOf(arrayOf("U", "O", "E"), arrayOf("E")),
            arrayOf(arrayOf("T", "S", "M", "E", "D"), arrayOf("T", "S", "M", "E")),
            arrayOf(
                arrayOf("T", "R", "D"), arrayOf("T", "R", "D")
            ),
            arrayOf(arrayOf("T", "R", "L", "B"), arrayOf("T", "R", "L")),
            arrayOf(arrayOf("T", "R", "L"), arrayOf("T", "R", "L")),
            arrayOf(
                arrayOf("T"), arrayOf("T")
            ),
            arrayOf(arrayOf("T"), arrayOf("T")),
            arrayOf(arrayOf("S", "R", "N", "M"), arrayOf("S", "R", "N", "M")),
            arrayOf(
                arrayOf("S"), arrayOf("S")
            ),
            arrayOf(arrayOf("S"), arrayOf("S")),
            arrayOf(arrayOf("S"), arrayOf("S")),
            arrayOf(
                arrayOf("R", "P"), arrayOf("R", "P")
            ),
            arrayOf(arrayOf("P"), arrayOf()),
            arrayOf(arrayOf("P", "O"), arrayOf()),
            arrayOf(
                arrayOf("O", "E"), arrayOf("E")
            ),
            arrayOf(arrayOf("O"), arrayOf()),
            arrayOf(arrayOf("O"), arrayOf()),
            arrayOf(arrayOf("O"), arrayOf()),
            arrayOf(
                arrayOf("N"), arrayOf()
            ),
            arrayOf(arrayOf("M"), arrayOf("M")),
            arrayOf(arrayOf("M"), arrayOf("M")),
            arrayOf(
                arrayOf("L"), arrayOf()
            ),
            arrayOf(arrayOf("L"), arrayOf("L")),
            arrayOf(arrayOf("I"), arrayOf()),
            arrayOf(
                arrayOf("I"), arrayOf()
            ),
            arrayOf(arrayOf("I", "E"), arrayOf("E")),
            arrayOf(arrayOf("I"), arrayOf()),
            arrayOf(
                arrayOf("I", "E"), arrayOf("E")
            ),
            arrayOf(arrayOf("I"), arrayOf()),
            arrayOf(arrayOf("H"), arrayOf()),
            arrayOf(arrayOf("H", "E"), arrayOf("H", "E")),
            arrayOf(
                arrayOf("H"), arrayOf("H")
            ),
            arrayOf(arrayOf("F"), arrayOf("F")),
            arrayOf(arrayOf("E"), arrayOf()),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(arrayOf("E"), arrayOf()),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(
                arrayOf("E", "A"), arrayOf("E")
            ),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(arrayOf("E"), arrayOf("E")),
            arrayOf(
                arrayOf("E"), arrayOf("E")
            ),
            arrayOf(arrayOf("D"), arrayOf("D")),
            arrayOf(arrayOf("A"), arrayOf()),
            arrayOf(
                arrayOf("A"), arrayOf()
            )
        )
    }
}