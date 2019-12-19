package eatyourbeets.console;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.animator.colorless.QuestionMark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.DynamicCard;
import eatyourbeets.cards.base.DynamicCardBuilder;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.utilities.JavaUtilities;

import java.util.Map;

public class DynamicCardData
{
    public String Base;
    public String Image;
    public String Name;
    public String Type;
    public String Rarity;
    public String Color;
    public Integer[] M;
    public Integer[] D;
    public Integer[] B;
    public Integer[] SV;
    public Integer[] Cost;
    public String[] Descriptions;

    public DynamicCard GenerateCard(String key, Map<String, DynamicCardData> cardPool)
    {
        DynamicCardBuilder builder = new DynamicCardBuilder(key);

        builder.SetText("", "", "");

        if (Base != null && !Base.equals(key))
        {
            if (cardPool.containsKey(Base))
            {
                Fill(cardPool.get(Base), builder);
            }
            else
            {
                Fill(CardLibrary.getCard(Base), builder);
            }
        }

        Fill(this, builder);

        if (builder.imagePath == null || !Gdx.files.internal(builder.imagePath).exists())
        {
            builder.imagePath = AnimatorResources.GetCardImage(QuestionMark.ID);
        }

        return builder.Build();
    }

    private static void Fill(DynamicCardData data, DynamicCardBuilder builder)
    {
        if (data.Name != null)
        {
            builder.cardStrings.NAME = data.Name;
        }

        if (data.Image != null)
        {
            builder.imagePath = data.Image;
        }

        if (data.Type != null)
        {
            builder.cardType = AbstractCard.CardType.valueOf(data.Type);
        }

        if (data.Rarity != null)
        {
            builder.cardRarity = AbstractCard.CardRarity.valueOf(data.Rarity);
        }

        if (data.Color != null)
        {
            builder.cardColor = AbstractCard.CardColor.valueOf(data.Color);
        }

        ArrayPair<String> stringPair = new ArrayPair<>(data.Descriptions);
        if (stringPair.t1 != null)
        {
            builder.cardStrings.DESCRIPTION = stringPair.t1;
        }
        if (stringPair.t2 != null)
        {
            builder.cardStrings.UPGRADE_DESCRIPTION = stringPair.t2;
        }

        ArrayPair<Integer> intPair = new ArrayPair<>(data.D);
        if (intPair.t1 != null)
        {
            builder.damage = intPair.t1;
        }
        if (intPair.t2 != null)
        {
            builder.upgradedDamage = intPair.t2;
        }

        intPair = new ArrayPair<>(data.M);
        if (intPair.t1 != null)
        {
            builder.magicNumber = intPair.t1;
        }
        if (intPair.t2 != null)
        {
            builder.upgradedMagicNumber = intPair.t2;
        }

        intPair = new ArrayPair<>(data.B);
        if (intPair.t1 != null)
        {
            builder.block = intPair.t1;
        }
        if (intPair.t2 != null)
        {
            builder.upgradedBlock = intPair.t2;
        }

        intPair = new ArrayPair<>(data.SV);
        if (intPair.t1 != null)
        {
            builder.secondaryValue = intPair.t1;
        }
        if (intPair.t2 != null)
        {
            builder.upgradedSecondaryValue = intPair.t2;
        }

        intPair = new ArrayPair<>(data.Cost);
        if (intPair.t1 != null)
        {
            builder.cost = intPair.t1;
        }
        if (intPair.t2 != null)
        {
            builder.upgradedCost = intPair.t2;
        }
    }

    private static void Fill(AbstractCard card, DynamicCardBuilder builder)
    {
        if (card == null)
        {
            return;
        }

        DynamicCardData data = new DynamicCardData();
        AbstractCard upgraded = card.makeCopy();
        upgraded.upgrade();

        data.Name = card.originalName;

        AnimatorCard ac = JavaUtilities.SafeCast(card, AnimatorCard.class);
        if (ac != null)
        {
            builder.SetSynergy(ac.GetSynergy(), ac.anySynergy);
            data.Image = AnimatorResources.GetCardImage(card.cardID);
        }
        else
        {
            data.Image = card.assetUrl;
        }

        data.Cost = new Integer[2];
        data.Cost[0] = card.cost;
        data.Cost[1] = upgraded.cost;

        data.Descriptions = new String[2];
        data.Descriptions[0] = card.rawDescription;
        data.Descriptions[1] = upgraded.rawDescription;

        data.Rarity = card.rarity.name();
        data.Type = card.type.name();
        data.Color = card.color.name();

        data.D = new Integer[2];
        data.D[0] = card.baseDamage;
        data.D[1] = upgraded.baseDamage;

        data.M = new Integer[2];
        data.M[0] = card.baseMagicNumber;
        data.M[1] = upgraded.baseMagicNumber;

        data.B = new Integer[2];
        data.B[0] = card.baseBlock;
        data.B[1] = upgraded.baseBlock;

        data.SV = new Integer[2];
        if (card instanceof EYBCard)
        {
            data.SV[0] = ((EYBCard)card).baseSecondaryValue;
            data.SV[1] = ((EYBCard)upgraded).baseSecondaryValue;
        }
        else
        {
            data.SV[0] = 0;
            data.SV[1] = 0;
        }

        Fill(data, builder);
    }

    private static class ArrayPair<T>
    {
        private T t1 = null;
        private T t2 = null;

        private ArrayPair(T[] arr)
        {
            if (arr != null)
            {
                if (arr.length > 0)
                {
                    t1 = arr[0];

                    if (arr.length > 1)
                    {
                        t2 = arr[1];
                    }
                }
            }
        }
    }
}
