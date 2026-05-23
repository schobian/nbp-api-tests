Feature: Kursy walut z API NBP

  Scenario Outline: Pobranie i filtrowanie kursów walut
    Given użytkownik pobiera tabelę kursów walut z API NBP
    Then odpowiedź API ma status 200

    And wyświetl kurs waluty o kodzie "<kodWaluty>"
    And wyświetl kurs waluty o nazwie "<nazwaWaluty>"

    And wyświetl waluty o kursie powyżej <kursPowyzej>
    And wyświetl waluty o kursie poniżej <kursPonizej>

    Examples:
      | kodWaluty | nazwaWaluty       | kursPowyzej  | kursPonizej  |
      | USD       | dolar amerykański | 5            | 3            |