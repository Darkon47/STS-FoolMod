 package eatyourbeets.console.commands;

 import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardMetadata;
import eatyourbeets.resources.GR;
 import eatyourbeets.ui.CustomCardLibSortHeader;
 import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.Testing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

 public class ParseGenericCommand extends ConsoleCommand
 {
     private static FieldInfo<Boolean> _isDeltaMultiplied;
     private static FieldInfo<Float> _deltaMultiplier;

     public static SpireConfig config;

     public ParseGenericCommand()
     {
         this.minExtraTokens = 1;
         this.maxExtraTokens = 1;
         this.simpleCheck = true;
     }

     @Override
     protected void execute(String[] tokens, int depth)
     {
         try
         {
             if (tokens.length > 1)
             {
                 if (tokens[1].equals("crop"))
                 {
                     Crop(tokens[2]);
                     return;
                 }

                 if (tokens[1].equals("show-special"))
                 {
                     CustomCardLibSortHeader.ShowSpecial = tokens.length > 2 && tokens[2].equals("true");
                     return;
                 }

                 if (tokens[1].equals("remove-colorless"))
                 {
                     CardLibrary.getAllCards().removeIf(card -> card.color == AbstractCard.CardColor.COLORLESS && !(card instanceof EYBCard));
                     return;
                 }

                 Test(tokens);
             }
         }
         catch (Exception ex)
         {
             DevConsole.log("Error: " + ex.getClass().getSimpleName());
         }
     }

     @Override
     public ArrayList<String> extraOptions(String[] tokens, int depth)
     {
         return new ArrayList<>();
     }

     public static void Test(String[] tokens) throws NumberFormatException
     {
         Float[] values = new Float[tokens.length-1];
         for (int i = 0; i < values.length; i++)
         {
             values[i] = Float.parseFloat(tokens[i+1]);
         }

         Testing.SetValues(values);
     }

     private static void Crop(String arg) throws IOException
     {
         final String path = "C:/temp/Animator-CardMetadata.json";
         final String jsonString = new String(Files.readAllBytes(Paths.get(path)));

         EYBCard card = GR.UI.CardPopup.GetCard();
         if (card != null)
         {
             Gson gson = new Gson();
             Map<String, EYBCardMetadata> items = GR.Animator.CardData;

             if (!items.containsKey(card.cardID))
             {
                 items.put(card.cardID, new EYBCardMetadata());
             }

             items.get(card.cardID).cropPortrait = arg.equals("true");

             Files.write(Paths.get(path), new Gson().toJson(items).getBytes());
         }
     }
 }
