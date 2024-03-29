package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.colorless.ShuichiSaihara;
import pinacolada.powers.FoolPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KaedeAkamatsu extends FoolCard
{
    public static final PCLCardData DATA = Register(KaedeAkamatsu.class).SetColorless()
    		.SetSkill(1, CardRarity.SPECIAL, PCLCardTarget.None, true).SetSeries(CardSeries.Danganronpa);

    public KaedeAkamatsu()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);

        PCLActions.Bottom.SelectFromPile(name, 1, p.exhaustPile)
                .SetFilter(PCLGameUtilities::HasBlueAffinity)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue)
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        PCLActions.Top.MoveCard(c, player.exhaustPile, player.hand)
                                .ShowEffect(true, true);
                        if (ShuichiSaihara.DATA.ID.equals(c.cardID)) {
                            PCLActions.Bottom.StackPower(new KaedeAkamatsuPower(p, secondaryValue));
                        }
                    }
                });
    }

    public static class KaedeAkamatsuPower extends FoolPower
    {

        public KaedeAkamatsuPower(AbstractCreature owner, int amount)
        {
            super(owner, KaedeAkamatsu.DATA);

            this.amount = amount;

            updateDescription();
        }
    }
}
