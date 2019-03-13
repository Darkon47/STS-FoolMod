package eatyourbeets.characters;

import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.AnimatorResources;
import eatyourbeets.Utilities;
import eatyourbeets.cards.animator.Defend;
import eatyourbeets.cards.animator.Strike;
import eatyourbeets.characters.Loadouts.*;

import java.util.ArrayList;
import java.util.StringJoiner;

public class AnimatorCharacterSelect
{
    private static int index = 0;
    private static final ArrayList<AnimatorCustomLoadout> customLoadouts = new ArrayList<>();

    protected static final String[] uiText = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.CharacterSelect).TEXT;

    public static AnimatorCustomLoadout GetSelectedLoadout()
    {
        return customLoadouts.get(index);
    }

    public static void NextLoadout()
    {
        index += 1;
        if (index >= customLoadouts.size())
        {
            index = 0;
        }
    }

    public static void PreviousLoadout()
    {
        index -= 1;
        if (index < 0)
        {
            index = customLoadouts.size() - 1;
        }
    }

    public static void OnTrueVictory(int ascensionLevel)
    {
        for (AnimatorCustomLoadout loadout : customLoadouts)
        {
            loadout.OnTrueVictory(GetSelectedLoadout(), ascensionLevel);
        }
        AnimatorMetrics.SaveTrophies();
    }

    private static void AddLoadout(AnimatorCustomLoadout loadout, int level, String description)
    {
        StringJoiner sj = new StringJoiner(", ");
        for (String s : loadout.GetStartingDeck())
        {
            if (!s.equals(Strike.ID) && !s.equals(Defend.ID))
            {
                sj.add(CardLibrary.getCardNameFromKey(s));
            }
        }
        loadout.description = sj.toString();
        //loadout.description = description;
        loadout.unlockLevel = level;
        customLoadouts.add(loadout);
    }

    static
    {
        String recommended = uiText[5];
        String balanced = uiText[6];
        String unbalanced = uiText[7];
        String veryUnbalanced = uiText[8];
        String special = uiText[9];

        AddLoadout(new Konosuba()           , 0, recommended);
        AddLoadout(new Gate()               , 1, balanced);
        AddLoadout(new Elsword()            , 1, balanced);
        AddLoadout(new NoGameNoLife()       , 1, balanced);
        AddLoadout(new OwariNoSeraph()      , 2, unbalanced);
        AddLoadout(new GoblinSlayer()       , 2, unbalanced);
        AddLoadout(new Katanagatari()       , 2, unbalanced);
        AddLoadout(new FullmetalAlchemist() , 2, unbalanced);
        AddLoadout(new Fate()               , 3, veryUnbalanced);
        AddLoadout(new Overlord()           , 3, veryUnbalanced);
        AddLoadout(new Chaika()             , 3, veryUnbalanced);
        AddLoadout(new Kancolle()           , 4, special);

        int synergyID = AnimatorMetrics.lastLoadout;

        Utilities.Logger.info("Last Layout: " + synergyID);

        //noinspection ConstantConditions
        for (int i = 0; i < customLoadouts.size(); i++)
        {
            Utilities.Logger.info(customLoadouts.get(i).Name + " (" + customLoadouts.get(i).ID + ")");
            if (synergyID == customLoadouts.get(i).ID)
            {
                index = i;
                break;
            }
        }
    }
}