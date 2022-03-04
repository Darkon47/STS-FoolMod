package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.*;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_TriggerOrb;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.Alibaba_Aladdin;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Alibaba extends FoolCard
{
    public static final PCLCardData DATA = Register(Alibaba.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Normal, PCLCardTarget.Normal)
            .SetColorless()
            .SetSeries(CardSeries.Magi)
            .PostInitialize(data -> data.AddPreview(new Alibaba_Aladdin(), false));

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Alibaba()
    {
        super(DATA);

        Initialize(3, 0, 2 , 2);
        SetUpgrade(0, 0, 0 , 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetHitCount(2);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Red, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL).forEach(d -> d.AddCallback(e -> {
            if (e.lastDamageTaken > 0) {
                if (choices.TryInitialize(this))
                {
                    choices.AddEffect(BaseEffect.ApplyToEnemies(magicNumber, PCLPowerHelper.Burning));
                    choices.AddEffect(new BaseEffect_TriggerOrb(1, PCLOrbHelper.Earth));
                }
                choices.Select(1, m);
            }
        }));

        int uniqueRareOrbs = 0;
        for (AbstractOrb orb : player.orbs)
        {
            if (PCLGameUtilities.IsValidOrb(orb) && !PCLGameUtilities.IsCommonOrb(orb))
            {
                uniqueRareOrbs += 1;
            }
        }
        if (uniqueRareOrbs >= secondaryValue && info.TryActivateLimited()) {
            PCLActions.Bottom.MakeCardInHand(new Alibaba_Aladdin());
            PCLActions.Last.Exhaust(this);
        }
    }

}