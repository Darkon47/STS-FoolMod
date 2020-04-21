package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Tohya extends AnimatorCard {
    public static final EYBCardData DATA = Register(Tohya.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);

    public Tohya() {
        super(DATA);

        Initialize(2, 6, 2,4);
        SetUpgrade(0, 4, 0);

        SetCooldown(magicNumber, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.GainBlock(block);

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
         GameActions.Bottom.GainBlock(secondaryValue).AddCallback(__ -> {
             GameActions.Bottom.DealDamageToRandomEnemy(player.currentBlock, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY);
         });
    }
}