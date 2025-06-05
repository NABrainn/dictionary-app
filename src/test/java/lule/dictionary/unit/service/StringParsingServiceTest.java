package lule.dictionary.unit.service;

import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.StringParsingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class StringParsingServiceTest {

    @Mock
    StringParsingService service = new StringParsingService();

    @Test
    void shouldSplitNonSpecialCharactersFromSpecialCharacters() {
      log.info("{}", service.toWhitespaceSplit("""
              Yr Gå til innholdet Et samarbeid mellom Meteorologisk institutt og NRK Søk Søk Meny Det ser ut til at du bruker en gammel nettleser. For å få tilgang til alt av innhold på Yr anbefaler vi at du oppdaterer nettleseren din. Det ser ut til at JavaScript er skrudd av i nettleseren din. For å få tilgang til alt av innhold på Yr anbefaler vi at du skrur på JavaScript. Forside Mine steder -- -- -- -- Værvarsel for over 10 millioner steder over hele verden Søk Relevant nå Kart Farevarsler Badetemperaturer Fjelloverganger Værsaker fra NRK Derfor kjem ikkje Spania-heten til Noreg Sommarvarmen kjem ikkje før i midten av juni. Publisert onsdag 4. juni Flaggstong knakk og vegar er stengde – det er varsla endå kraftigare vindkast tysdag Både Festplassen i Bergen sentrum og hovudvegar ved Haukeland universitetssjukehus er sperra fordi sterke vindkast knekker tre – og ei flaggstong. Publisert tirsdag 3. juni Togprosjektet frykter været: – Vi tåler noen timer Bane Nor er i rute, men ting de ikke kontrollerer kan få prosjektet til å spore av: Aller mest frykter de tordenvær. Publisert lørdag 31. mai Andre tjenester fra Meteorologisk institutt Meteorologens prognosekart Tekstvarsel for Norge Satellittbilder Bunntekst Se alle hjelpeartikler Kontakt oss Choose language / velg språk BokmålNynorskDavvisámegiellaEnglish Yr er et samarbeid mellom Opphavsrett © NRK og Meteorologisk institutt 2007–2025 Redaktør: Ingrid Støver Jensen Ansvarlig redaktør: Vibeke Fürst Haugen Meteorologisk ansvarlig: Roar Skålin Personvern Informasjonskapsler Samarbeidspartnere App for iOS App for Android Yr for utviklere Badetemperaturer Yr på Facebook Yr på Twitter Yr på Instagram
              """));
    }
}
