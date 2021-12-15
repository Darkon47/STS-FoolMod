package eatyourbeets.cards.animator.enchantments;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardPreview;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.relics.animator.UsefulBox;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class Enchantment extends AnimatorCard implements Hidden
{
    public static final String ID = GR.Animator.CreateID(Enchantment.class.getSimpleName());
    private static final ArrayList<Enchantment> cards = new ArrayList<>();

    public int index;
    public boolean requiresTarget;
    public EnchantableRelic relic;

    private final Color borderColor;

    public static EYBCardData RegisterInternal(Class<? extends AnimatorCard> type)
    {
        return Register(type).SetPower(-2, CardRarity.SPECIAL).SetImagePath(GR.GetCardImage(ID));
    }

    public static ArrayList<Enchantment> GetCards()
    {
        if (cards.isEmpty())
        {
            cards.add(new Enchantment1());
            cards.add(new Enchantment2());
            cards.add(new Enchantment3());
            cards.add(new Enchantment4());
            cards.add(new Enchantment5());

            for (Enchantment a : cards)
            {
                for (Enchantment b : Enchantment.GetCard(a.index, 0).GetUpgrades())
                {
                    a.cardData.AddPreview(b, true);
                }
            }
        }

        return cards;
    }

    public static Enchantment GetCard(int index, int upgradeIndex)
    {
        for (Enchantment e : GetCards())
        {
            if (e.index == index)
            {
                Enchantment result = (Enchantment) e.makeCopy();
                if (upgradeIndex > 0)
                {
                    result.auxiliaryData.form = upgradeIndex;
                    result.upgrade();
                }

                return result;
            }
        }

        throw new IndexOutOfBoundsException("Enchantment not found at index: " + index);
    }

    protected Enchantment(EYBCardData cardData, int index)
    {
        super(cardData);

        this.index = index;
        this.borderColor = new Color(0.7f, 0.8f, 0.9f, 1f);
        this.cropPortrait = false;
        this.relic = new UsefulBox(this);
        this.portraitImg = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(ID), true));
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        if (upgraded && auxiliaryData.form > 0)
        {
            return JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form - 1], args);
        }
        else
        {
            return JUtils.Format(cardData.Strings.DESCRIPTION, args);
        }
    }

    public int GetPowerCost()
    {
        return secondaryValue;
    }

    public boolean CanUsePower(int cost)
    {
        return EnergyPanel.getCurrentEnergy() >= cost;
    }

    public void PayPowerCost(int cost)
    {
        EnergyPanel.useEnergy(cost);
    }

    public abstract int GetMaxUpgradeIndex();
    public abstract void UsePower(AbstractMonster m);

    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return upgraded ? null : super.GetCardPreview();
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return super.GetCardBanner().SetColor(borderColor);
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        return super.GetPortraitFrame().SetColor(borderColor);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        return GetCard(index, auxiliaryData.form);
    }

    public ArrayList<Enchantment> GetUpgrades()
    {
        final ArrayList<Enchantment> result = new ArrayList<>();
        for (int i = 1; i <= GetMaxUpgradeIndex(); i++)
        {
            result.add(GetCard(index, i));
        }

        return result;
    }
}