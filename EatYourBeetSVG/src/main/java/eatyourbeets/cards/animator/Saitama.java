package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.JavaUtilities;

public class Saitama extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(Saitama.class.getSimpleName(), EYBCardBadge.Special);

    private int stage;

    public Saitama()
    {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);

        Initialize(0, 0);

        this.misc = 0;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (stage != misc)
        {
            stage = misc = Math.max(Math.min(misc, 5), 0);
            SetEffect(stage);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AbstractCard card = super.makeStatEquivalentCopy();

        Saitama other = JavaUtilities.SafeCast(card, Saitama.class);
        if (other != null)
        {
            other.misc = other.stage = this.misc;
            other.SetEffect(stage);
        }

        return card;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        switch (stage)
        {
            case 0:
            {
                // Do Nothing

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                GameActionsHelper.DrawCard(p, magicNumber);
                GameActionsHelper.GainAgility(secondaryValue);

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                GameActionsHelper.ApplyPower(p, p, new BufferPower(p, 1), 1);

                break;
            }

            case 3:
            {
                // Gain !M! Force. Gain !B! Block.
                GameActionsHelper.GainForce(magicNumber);
                GameActionsHelper.GainBlock(p, block);

                break;
            }

            case 4:
            {
                // Deal !D! damage !M! times.
                for (int i = 1; i < this.magicNumber; i++)
                {
                    GameActionsHelper.AddToDefault(new PummelDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                }

                GameActionsHelper.AddToDefault(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

                break;
            }

            case 5:
            {
                // Remove Intangible. Deal !D! damage. Stun The Enemy
                GameActionsHelper.AddToDefault(new RemoveSpecificPowerAction(m, p, IntangiblePower.POWER_ID));
                GameActionsHelper.AddToDefault(new RemoveSpecificPowerAction(m, p, IntangiblePlayerPower.POWER_ID));

//                GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.SMASH);

                GameActionsHelper.AddToDefault(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
                GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.NONE);
                GameActionsHelper.AddToDefault(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

                GameActionsHelper.ApplyPowerSilently(p, m, new StunMonsterPower(m, 1), 1);

                break;
            }
        }

        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        if (state == this && action != null)
        {
            GameActionsHelper.AddToBottom(new ProgressPhaseAction(this));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.isInnate = true;
        }
    }

    private void SetEffect(int stage)
    {
        switch (stage)
        {
            case 0:
            {
                // Do Nothing
                this.cardText.OverrideDescription(null, true);

                Initialize(0, 0, 0, 0);

                this.target = CardTarget.NONE;
                this.type = CardType.SKILL;

                this.loadCardImage(Resources_Animator.GetCardImage(ID));

                break;
            }

            case 1:
            {
                // Draw !M! Cards. NL Gain !SV! Agility.
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[0], true);

                Initialize(0, 0, 3, 2);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_0"));

                break;
            }

            case 2:
            {
                // Prevent the next time you would lose HP
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[1], true);

                Initialize(0, 8, 0, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_1"));

                break;
            }

            case 3:
            {
                // Gain !M! Force. Gain !B! Block
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[2], true);

                Initialize(0, 9, 6, 0);

                this.target = CardTarget.SELF;
                this.type = CardType.SKILL;

                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_2"));

                break;
            }

            case 4:
            {
                // Deal !D! damage !M! times.
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[3], true);

                Initialize(6, 0, 8, 0);

                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;

                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_3"));

                break;
            }

            case 5:
            {
                // Remove Intangible. Deal !D! damage.
                this.cardText.OverrideDescription(cardData.strings.EXTENDED_DESCRIPTION[4], true);

                Initialize(1000, 0, 0, 0);

                this.target = CardTarget.ENEMY;
                this.type = CardType.ATTACK;

                this.loadCardImage(Resources_Animator.GetCardImage(ID + "_4"));

                break;
            }
        }

        this.upgradeBaseCost(stage);
    }

    private class ProgressPhaseAction extends AnimatorAction
    {
        private final Saitama saitama;

        private ProgressPhaseAction(Saitama saitama)
        {
            this.saitama = saitama;
        }

        @Override
        public void update()
        {
            if (saitama.misc < 5)
            {
                saitama.misc += 1;
                saitama.applyPowers();
            }

            this.isDone = true;
        }
    }
}