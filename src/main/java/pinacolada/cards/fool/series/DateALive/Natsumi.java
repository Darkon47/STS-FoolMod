package pinacolada.cards.fool.series.DateALive;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Natsumi extends FoolCard
{
    public static final PCLCardData DATA = Register(Natsumi.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Ice, PCLCardTarget.Random)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public Natsumi()
    {
        super(DATA);

        Initialize(2, 0, 2, 2);
        SetUpgrade(1,0, 1, 0);
        SetAffinity_Blue(1, 0, 2);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Blue, 7);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            Initialize(2, 0, 2, 2);
            SetUpgrade(1,0, 0, 1);
        }
        else {
            Initialize(2, 0, 2, 2);
            SetUpgrade(1,0, 1, 0);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.FIRE).forEach(d -> d
        .SetVFXColor(Color.SKY, Color.SKY));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            boolean upgrade = TrySpendAffinity(PCLAffinity.Blue);
            int gainAmount = 0;
            for (AbstractCard card : cards)
            {
                if (!PCLGameUtilities.IsHindrance(card)) {
                    gainAmount += secondaryValue;
                }
                AbstractCard replacement = AbstractDungeon.getCard(CardRarity.UNCOMMON);
                if (upgrade) {
                    replacement.upgrade();
                }
                PCLActions.Bottom.ReplaceCard(card.uuid, replacement);
            }
            if (gainAmount > 0) {
                PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Sorcery, gainAmount);
            }
        });

    }
}