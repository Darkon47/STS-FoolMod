package pinacolada.patches.cardLibrary;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.animator.special.OrbCore_Aether;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardBase;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.ReplacementCardBuilder;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.cards.fool.special.OrbCore_Air;
import pinacolada.cards.pcl.curse.*;
import pinacolada.cards.pcl.status.*;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.Map;

public class PCLCardLibraryPatches
{
    // TODO keep this value in sync with the actual beta mod
    private static final String AnimatorBetaID = "animatorbeta";
    private static final FieldInfo<HashMap<String, AbstractCard>> _curses = PCLJUtils.GetField("curses", CardLibrary.class);
    private static final byte[] whatever = {0x61, 0x6e, 0x69, 0x6d, 0x61, 0x74, 0x6f, 0x72, 0x3a, 0x75, 0x72, 0x3a};
    private static final String idPrefix = new String(whatever);

    public static EYBCardData GetEYBCardReplacement(String cardID) {
        // Attempt to find the Animator replacement for PCL cards
        if (cardID.startsWith(PCLResources.ID)) {
            String replacementID = cardID.replace(PCLResources.ID, AnimatorResources.ID);
            EYBCardData replacementData = EYBCard.GetStaticData(replacementID);
            if (replacementData == null) {
                replacementID = cardID.replace(PCLResources.ID, AnimatorBetaID);
                replacementData = EYBCard.GetStaticData(replacementID);
            }
            return replacementData;
        }

        // Misc. PCL game card replacements
        if (OrbCore_Air.DATA.ID.equals(cardID)) {
            return OrbCore_Aether.DATA;
        }
        return null;
    }

    public static PCLCardData GetPCLCardReplacement(String cardID) {
        // Attempt to find the PCL replacement for AnimatorBeta cards
        if (cardID.startsWith(AnimatorBetaID)) {
            String replacementID = cardID.replace(AnimatorBetaID, PCLResources.ID);
            return PCLCard.GetStaticData(replacementID);
        }

        // Attempt to find the PCL replacement for EYB/Animator cards
        if (cardID.startsWith(AnimatorResources.ID)) {
            String replacementID = cardID.replace(AnimatorResources.ID, PCLResources.ID);
            return PCLCard.GetStaticData(replacementID);
        }

        // Misc. Animator game card replacements
        if (OrbCore_Aether.DATA.ID.equals(cardID)) {
            return OrbCore_Air.DATA;
        }
        return null;
    }

    public static PCLCardData GetStandardReplacement(String cardID)
    {
        // Base game card replacements
        switch (cardID)
        {
            case Apparition.ID: return pinacolada.cards.pcl.replacement.Apparition.DATA;
            case AscendersBane.ID:
                return Curse_AscendersBane.DATA;
            case Bite.ID: return pinacolada.cards.pcl.replacement.Bite.DATA;
            case Burn.ID: return Status_Burn.DATA;
            case Clumsy.ID: return Curse_Clumsy.DATA;
            case Dazed.ID: return Status_Dazed.DATA;
            case Decay.ID: return Curse_Decay.DATA;
            case Doubt.ID: return Curse_Doubt.DATA;
            case Enlightenment.ID: return pinacolada.cards.pcl.replacement.Enlightenment.DATA;
            case Injury.ID: return Curse_Injury.DATA;
            case JAX.ID: return pinacolada.cards.pcl.replacement.JAX.DATA;
            case Miracle.ID: return pinacolada.cards.pcl.replacement.Miracle.DATA;
            case Normality.ID: return Curse_Normality.DATA;
            case Pain.ID: return Curse_Pain.DATA;
            case Parasite.ID: return Curse_Parasite.DATA;
            case Regret.ID: return Curse_Regret.DATA;
            case RitualDagger.ID: return pinacolada.cards.pcl.replacement.RitualDagger.DATA;
            case Shame.ID: return Curse_Shame.DATA;
            case Slimed.ID: return Status_Slimed.DATA;
            case VoidCard.ID: return Status_Void.DATA;
            case Wound.ID: return Status_Wound.DATA;
            case Writhe.ID: return Curse_Writhe.DATA;

            default: return null;
        }
    }

    protected static AbstractCard TryCreateReplacementForPCL(AbstractCard card) {
        if (card instanceof PCLCardBase) {
            return card;
        }
        PCLCardData data = GetStandardReplacement(card.cardID);
        if (data != null)
        {
            return data.MakeCopy(card.upgraded);
        }
        else if (PGR.PCL.Config.ReplaceCardsFool.Get()) {
            data = GetPCLCardReplacement(card.cardID);
            if (data != null)
            {
                return data.MakeCopy(card.upgraded);
            }
            else {
                AbstractCard c = new ReplacementCardBuilder(card, true).Build();
                if (card.upgraded) {
                    c.upgrade();
                }
                return c;
            }
        }
        return card;
    }

    public static void TryReplace(AbstractCard[] card)
    {
        if (PCLGameUtilities.IsPCLPlayerClass()) {
            card[0] = TryCreateReplacementForPCL(card[0]);
        }
        else if (PGR.PCL.Config.ReplaceCardsAnimator.Get() && PCLGameUtilities.IsPlayerClass(eatyourbeets.resources.GR.Animator.PlayerClass))
        {
            final EYBCardData data = GetEYBCardReplacement(card[0].cardID);
            if (data != null)
            {
                card[0] = data.MakeCopy(card[0].upgraded);
            }
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
    public static class CardLibraryPatches_GetCard
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(String key)
        {
            if (key.startsWith(idPrefix))
            {
                PCLLoadout loadout = PGR.PCL.Data.GetByName(key.replace(idPrefix, ""));
                if (loadout != null && loadout.GetUltraRare() != null)
                {
                    key = loadout.GetUltraRare().ID;
                }
            }

            if (PGR.IsLoaded())
            {
                if (FoolCard_UltraRare.IsSeen(key))
                {
                    final AbstractCard card = FoolCard_UltraRare.GetCards().get(key);
                    if (card != null)
                    {
                        return SpireReturn.Return(card.makeCopy());
                    }
                }
                else if (PCLGameUtilities.IsPCLPlayerClass())
                {
                    final PCLCardData data = GetStandardReplacement(key);
                    if (data != null)
                    {
                        return SpireReturn.Return(data.MakeCopy(false));
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCopy", paramtypez = {String.class, int.class, int.class})
    public static class CardLibraryPatches_GetCopy
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(String key, int upgradeTime, int misc)
        {
            AbstractCard card = FoolCard_UltraRare.GetCards().get(key);
            if (card != null)
            {
                card = card.makeCopy();
                card.misc = misc;

                for (int i = 0; i < upgradeTime; ++i)
                {
                    card.upgrade();
                }

                return SpireReturn.Return(card);
            }
            else if (key.equals(AscendersBane.ID) && PCLGameUtilities.IsPCLPlayerClass())
            {
                return SpireReturn.Return(Curse_AscendersBane.DATA.MakeCopy(false));
            }
            else if (PCLGameUtilities.IsPCLPlayerClass()) {
                PCLCardData data = GetStandardReplacement(key);
                if (data == null && PGR.PCL.Config.ReplaceCardsFool.Get()) {
                    data = GetPCLCardReplacement(key);
                }
                if (data != null)
                {
                    return SpireReturn.Return(data.MakeCopy(upgradeTime > 0));
                }
            }

            return SpireReturn.Continue();
        }

        @SpirePostfixPatch
        public static AbstractCard Postfix(AbstractCard __result)
        {
            if (PGR.PCL.Config.ReplaceCardsFool.Get() && PCLGameUtilities.IsPCLPlayerClass()) {
                return TryCreateReplacementForPCL(__result);
            }
            return __result;
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {})
    public static class CardLibraryPatches_GetCurse
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix()
        {
            return CardLibraryPatches_GetCurse2.Postfix(null, AbstractDungeon.cardRng);
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {AbstractCard.class, Random.class})
    public static class CardLibraryPatches_GetCurse2
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Postfix(AbstractCard ignore, Random rng)
        {
            final RandomizedList<String> cards = new RandomizedList<>();
            for (Map.Entry<String, AbstractCard> entry : _curses.Get(null).entrySet())
            {
                final AbstractCard c = entry.getValue();
                final PCLCardData replacement = (PGR.IsLoaded() && PCLGameUtilities.IsPCLPlayerClass() && PGR.PCL.Config.ReplaceCardsFool.Get()) ? GetStandardReplacement(c.cardID) : null;
                if (c.rarity != AbstractCard.CardRarity.SPECIAL && (ignore == null || !c.cardID.equals(ignore.cardID)) && replacement == null)
                {
                    cards.Add(entry.getKey());
                }
            }

            return SpireReturn.Return(CardLibrary.cards.get(cards.Retrieve(rng)));
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard", paramtypez = {AbstractCard.CardType.class, AbstractCard.CardRarity.class})
    public static class CardLibraryPatches_GetAnyColorCard
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(AbstractCard.CardType type, AbstractCard.CardRarity rarity)
        {
            if (PCLGameUtilities.IsPCLPlayerClass() || PCLGameUtilities.IsEYBPlayerClass()) {
                return SpireReturn.Return(PCLGameUtilities.GetAnyColorCardFiltered(rarity, type, false));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard", paramtypez = {AbstractCard.CardRarity.class})
    public static class CardLibraryPatches_GetAnyColorCard2
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(AbstractCard.CardRarity rarity)
        {
            if (PCLGameUtilities.IsPCLPlayerClass() || PCLGameUtilities.IsEYBPlayerClass()) {
                return SpireReturn.Return(PCLGameUtilities.GetAnyColorCardFiltered(rarity, null, true));
            }
            return SpireReturn.Continue();
        }
    }
}
